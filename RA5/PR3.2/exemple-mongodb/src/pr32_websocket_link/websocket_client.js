const WebSocket = require('ws');
const readline = require('readline');
const { v4: uuidv4 } = require('uuid');

// Configuració
const SERVER_URL = 'ws://localhost:8080';
let sessionId = uuidv4(); // Identificador únic per aquesta partida
const playerId = `player_${Math.floor(Math.random() * 1000)}`;

// Estat del joc
let currentPosition = { x: 0, y: 0 };
let lastMoveTime = Date.now();
let isGameActive = true;
let movementCount = 0;

// Connectar al servidor
const ws = new WebSocket(SERVER_URL);

// Configurar lectura de teclat
readline.emitKeypressEvents(process.stdin);
process.stdin.setRawMode(true);
process.stdin.resume();

console.log(` Client de joc iniciat`);
console.log(`Session ID: ${sessionId}`);
console.log(`Player ID: ${playerId}`);
console.log(`Posició inicial: (${currentPosition.x}, ${currentPosition.y})`);
console.log(`\n=== Controls ===`);
console.log(`  ↑ - Moure amunt (Y+1)`);
console.log(`  ↓ - Moure avall (Y-1)`);
console.log(`  ← - Moure esquerra (X-1)`);
console.log(`  → - Moure dreta (X+1)`);
console.log(`  N - Nova partida (genera nou Session ID)`);
console.log(`  Q - Sortir`);
console.log(`\n[i] La partida finalitzarà automàticament després de 10 segons sense moviment.\n`);

// Enviar posició inicial
function sendPosition() {
    if (!isGameActive) return;
    
    const message = {
        sessionId,
        playerId,
        position: currentPosition,
        timestamp: new Date().toISOString()
    };
    
    ws.send(JSON.stringify(message));
}

// Processar tecles
process.stdin.on('keypress', (str, key) => {
    if (key.name == 'q') {
        console.log(`\n[q] Sortint...`);
        process.exit(0);
        return;
    }
    
    if (!isGameActive) {
        if (key.name === 'n') {
            // Nova partida
            sessionId = uuidv4();
            isGameActive = true;
            currentPosition = { x: 0, y: 0 };
            movementCount = 0;
            console.log(`\n+++ Nova partida iniciada! Session ID: ${sessionId}`);
            sendPosition();
        }
        return;
    }
    
    switch (key.name) {
        case 'up':
            currentPosition.y += 1;
            movementCount++;
            console.log(`↑↑↑ Moviment ${movementCount}: (${currentPosition.x}, ${currentPosition.y})`);
            sendPosition();
            break;
        case 'down':
            currentPosition.y -= 1;
            movementCount++;
            console.log(`↓↓↓ Moviment ${movementCount}: (${currentPosition.x}, ${currentPosition.y})`);
            sendPosition();
            break;
        case 'left':
            currentPosition.x -= 1;
            movementCount++;
            console.log(`←←← Moviment ${movementCount}: (${currentPosition.x}, ${currentPosition.y})`);
            sendPosition();
            break;
        case 'right':
            currentPosition.x += 1;
            movementCount++;
            console.log(`→→→ Moviment ${movementCount}: (${currentPosition.x}, ${currentPosition.y})`);
            sendPosition();
            break;
        case 'n':
            console.log(`\n[n] Iniciant nova partida...`);
            sessionId = uuidv4();
            currentPosition = { x: 0, y: 0 };
            movementCount = 0;
            console.log(`Nova Session ID: ${sessionId}`);
            break;
    }
});

// Event: Connexió establerta
ws.on('open', () => {
    console.log(`>>> Connectat al servidor WebSocket: ${SERVER_URL}`);
    sendPosition(); // Enviar posició inicial
});

// Event: Rebre missatge del servidor
ws.on('message', (data) => {
    try {
        const message = JSON.parse(data.toString());
        
        switch (message.type) {
            case 'movement_received':
                // Confirmació rebuda
                break;
            case 'game_over':
                console.log(`\n PARTIDA FINALITZADA!`);
                console.log(`    Distància recorreguda: ${message.distance} unitats`);
                console.log(`    Moviments totals: ${message.totalMovements}`);
                console.log(`    Durada: ${(message.duration / 1000).toFixed(2)} segons`);
                console.log(`    Inici: (${message.startPosition.x}, ${message.startPosition.y})`);
                console.log(`    Final: (${message.endPosition.x}, ${message.endPosition.y})`);
                console.log(`\n Prem 'N' per començar una nova partida o 'Q' per sortir\n`);
                isGameActive = false;
                break;
            case 'error':
                console.log(`!!! Error del servidor: ${message.message}`);
                break;
        }
    } catch (error) {
        console.error(`Error processant missatge: ${error.message}`);
    }
});

// Event: Error de connexió
ws.on('error', (error) => {
    console.error(`!!! Error WebSocket: ${error.message}`);
});

// Event: Desconnexió
ws.on('close', () => {
    console.log(`<<< Desconnectat del servidor`);
    isGameActive = false;
});

// Gestionar tancament del programa
process.on('SIGINT', () => {
    console.log(`\n<<< Sortint...`);
    ws.close();
    process.exit(0);
});