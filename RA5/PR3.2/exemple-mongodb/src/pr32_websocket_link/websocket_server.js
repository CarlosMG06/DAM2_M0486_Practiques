const { webcrypto } = require('crypto');
if (!globalThis.crypto) {
    globalThis.crypto = webcrypto;
}

const WebSocket = require('ws');
const { MongoClient } = require('mongodb');
const winston = require('winston');
const path = require('path');
const fs = require('fs');
require('dotenv').config();

const logger = require('./logger');

const PORT = 8080;
const MONGODB_URI = 'mongodb://localhost:27017/';
const DB_NAME = 'game_db';
const MOVEMENTS_COLLECTION = 'movements';
const INACTIVITY_TIMEOUT = 10000;

// Sessions actives
const activeSessions = new Map();

// Variables globals
let db;
let movementsCollection;
let mongoClient;

// Connectar a MongoDB
async function connectMongoDB() {
    try {
        logger.info('Connectant a MongoDB...');
        mongoClient = new MongoClient(MONGODB_URI);
        await mongoClient.connect();
        db = mongoClient.db(DB_NAME);
        movementsCollection = db.collection(MOVEMENTS_COLLECTION);
        
        // Verificar que la conexión funciona
        await movementsCollection.findOne({});
        
        logger.info(`>>> Connectat a MongoDB: ${DB_NAME}`);
        logger.info(`>>> Col·lecció: ${MOVEMENTS_COLLECTION}`);
        return true;
    } catch (error) {
        logger.error(`!!! Error connectant a MongoDB: ${error.message}`);
        throw error;
    }
}

function calculateDistance(pos1, pos2) {
    const dx = pos2.x - pos1.x;
    const dy = pos2.y - pos1.y;
    return Math.sqrt(dx * dx + dy * dy).toFixed(2);
}

// Finalitzar partida
async function endGameSession(sessionId) {
    const session = activeSessions.get(sessionId);
    if (!session) return null;
    
    const { movements, startTime, startPosition, lastPosition, playerId } = session;
    const endTime = Date.now();
    const duration = endTime - startTime;
    const distance = calculateDistance(startPosition, lastPosition);
    
    logger.info(`=== Partida finalitzada === `);
    logger.info(` SessionID: ${sessionId}`);
    logger.info(` Distància: ${distance}`);
    logger.info(` Moviments: ${movements.length}`);
    logger.info(` Durada: ${duration}ms`);
    
    // Guardar resum de la partida a MongoDB
    try {
        if (movementsCollection) {
            const gameSummary = {
                type: 'game_summary',
                sessionId,
                playerId,
                startTime: new Date(startTime),
                endTime: new Date(endTime),
                duration,
                startPosition,
                endPosition: lastPosition,
                distance: parseFloat(distance),
                totalMovements: movements.length
            };
            
            await movementsCollection.insertOne(gameSummary);
            logger.debug(`Resum de partida guardat a MongoDB: ${sessionId}`);
        } else {
            logger.error('movementsCollection no disponible per guardar resum');
        }
    } catch (error) {
        logger.error(`Error guardant resum a MongoDB: ${error.message}`);
    }
    
    // Netejar timeout si existeix
    if (session.timeout) {
        clearTimeout(session.timeout);
    }
    
    // Eliminar de sessions actives
    activeSessions.delete(sessionId);
    
    return {
        type: 'game_over',
        sessionId,
        distance,
        totalMovements: movements.length,
        duration,
        startPosition,
        endPosition: lastPosition
    };
}

// Timeout d'inactivitat
function setTimeoutForSession(sessionId) {
    const session = activeSessions.get(sessionId);
    if (!session) return;
    
    if (session.timeout) {
        clearTimeout(session.timeout);
    }
    
    session.timeout = setTimeout(async () => {
        logger.info(`Timeout activat per sessió: ${sessionId}`);
        const result = await endGameSession(sessionId, 'inactivity_timeout');
        if (result) {
            // Enviar resultat al client si encara està connectat
            const clients = wss.clients;
            clients.forEach(client => {
                if (client.readyState === WebSocket.OPEN && client.sessionId === sessionId) {
                    client.send(JSON.stringify(result));
                }
            });
        }
    }, INACTIVITY_TIMEOUT);
}

// Crear servidor WebSocket
let wss;

async function startServer() {
    // Primero conectar a MongoDB
    await connectMongoDB();
    
    // Luego iniciar el servidor WebSocket
    wss = new WebSocket.Server({ port: PORT });
    
    wss.on('listening', () => {
        logger.info(`>>> Servidor WebSocket escoltant al port ${PORT}`);
    });
    
    wss.on('connection', (ws, req) => {
        const clientIp = req.socket.remoteAddress;
        logger.info(`>>> Nou client connectat: ${clientIp}`);
        
        ws.on('message', async (data) => {
            try {
                const message = JSON.parse(data.toString());
                logger.debug(`Missatge rebut: ${JSON.stringify(message)}`);
                
                const { sessionId, playerId, position, timestamp } = message;
                
                // Validació
                if (!sessionId || !playerId || !position || position.x === undefined || position.y === undefined) {
                    logger.warn(`Missatge invàlid de ${clientIp}: ${JSON.stringify(message)}`);
                    ws.send(JSON.stringify({ type: 'error', message: 'Invalid message format' }));
                    return;
                }
                
                // Verificar que MongoDB està connectat
                if (!movementsCollection) {
                    logger.error('MongoDB no està connectat');
                    ws.send(JSON.stringify({ type: 'error', message: 'Database not connected' }));
                    return;
                }
                
                // Inicialitzar o actualitzar sessió
                if (!activeSessions.has(sessionId)) {
                    // Nova partida
                    activeSessions.set(sessionId, {
                        movements: [],
                        startTime: Date.now(),
                        lastMoveTime: Date.now(),
                        playerId,
                        startPosition: { x: position.x, y: position.y },
                        lastPosition: { x: position.x, y: position.y },
                        timeout: null
                    });
                    logger.info(`+++ Nova partida iniciada - Session: ${sessionId}, Player: ${playerId}, Posició inicial: (${position.x}, ${position.y})`);
                }
                
                const session = activeSessions.get(sessionId);
                
                // Emmagatzemar moviment a MongoDB
                const movementDoc = {
                    sessionId,
                    playerId,
                    movementNumber: session.movements.length + 1,
                    position: { x: position.x, y: position.y },
                    previousPosition: session.lastPosition,
                    distanceFromPrevious: parseFloat(calculateDistance(session.lastPosition, position)),
                    timestamp: new Date(timestamp || Date.now()),
                    receivedAt: new Date()
                };
                
                try {
                    await movementsCollection.insertOne(movementDoc);
                    logger.info(`+++ Moviment ${session.movements.length + 1} guardat - Session: ${sessionId} - Posició: (${position.x}, ${position.y})`);
                } catch (dbError) {
                    logger.error(`!!! Error guardant moviment a MongoDB: ${dbError.message}`);
                    ws.send(JSON.stringify({ type: 'error', message: 'Database error' }));
                    return;
                }
                
                // Actualitzar sessió
                session.movements.push(movementDoc);
                session.lastPosition = { x: position.x, y: position.y };
                session.lastMoveTime = Date.now();
                
                setTimeoutForSession(sessionId);
                
                // Associar sessionId al WebSocket per poder enviar resultats després
                ws.sessionId = sessionId;
                
                // Enviar confirmació al client
                ws.send(JSON.stringify({
                    type: 'movement_received',
                    sessionId,
                    movementCount: session.movements.length
                }));
                
            } catch (error) {
                logger.error(`Error processant missatge: ${error.message}`);
                ws.send(JSON.stringify({ type: 'error', message: 'Internal server error' }));
            }
        });
        
        ws.on('close', () => {
            logger.info(`<<< Client desconnectat: ${clientIp}`);
            // No finalitzem la partida immediatament per si es reconnecta
            // El timeout ho gestiona
        });
        
        ws.on('error', (error) => {
            logger.error(`!!! Error amb client ${clientIp}: ${error.message}`);
        });
    });
    
    wss.on('error', (error) => {
        logger.error(`!!! Error del servidor WebSocket: ${error.message}`);
    });
    
    logger.info(`>>> Servidor preparat. Timeout d'inactivitat: ${INACTIVITY_TIMEOUT}ms`);
}

// Tancament graceful
process.on('SIGINT', async () => {
    logger.info('<<< Tancant servidor...');

    for (const [sessionId] of activeSessions) {
        await endGameSession(sessionId, 'server_shutdown');
    }
    if (wss) {
        wss.close(() => {
            logger.info('WebSocket tancat');
        });
    }
    if (mongoClient) {
        await mongoClient.close();
        logger.info('MongoDB tancat');
    }
    
    logger.info('<<< Servidor finalitzat');
    process.exit(0);
});


startServer().catch(error => {
    logger.error(`!!! Error iniciant servidor: ${error.message}`);
    process.exit(1);
});