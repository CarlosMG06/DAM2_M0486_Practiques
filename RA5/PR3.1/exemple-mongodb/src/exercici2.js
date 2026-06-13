const { webcrypto } = require('crypto');
if (!globalThis.crypto) {
  globalThis.crypto = webcrypto;
}

const { MongoClient } = require('mongodb');
const PDFDocument = require('pdfkit');
const fs = require('fs');
const path = require('path');
require('dotenv').config();

const logger = require('./logger');

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/';
const DB_NAME = 'stackexchange_db';
const COLLECTION_NAME = 'questions';
const OUTPUT_DIR = path.join(__dirname, '../../data/out');

// Assegurar que el directori de sortida existeix
if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
}

const TITLE_KEYWORDS = ["pug", "wig", "yak", "nap", "jig", "mug", "zap", "gag", "oaf", "elf"];

async function generatePDF(titles, outputPath, title) {
    return new Promise((resolve, reject) => {
        const doc = new PDFDocument({ margin: 50, size: 'A4' });
        const stream = fs.createWriteStream(outputPath);
        
        doc.pipe(stream);
        
        // Títol principal
        doc.fontSize(20)
           .font('Helvetica-Bold')
           .text(title, { align: 'center' })
           .moveDown(0.5);
        
        // Subtítol amb total
        doc.fontSize(12)
           .font('Helvetica')
           .text(`Total de resultats: ${titles.length}`, { align: 'left' })
           .moveDown(0.5);
        
        // Llistat de títols (numerats)
        if (titles.length === 0) {
            doc.fontSize(12)
               .text('No s\'han trobat resultats per a aquesta consulta.', { align: 'center' });
        } else {
            titles.forEach((item, index) => {
                doc.fontSize(10)
                   .font('Helvetica')
                   .text(`${index + 1}. ${item}`, {
                       indent: 20,
                       paragraphGap: 5,
                       lineGap: 3
                   });
            });
        }
        
        doc.end();
        
        stream.on('finish', () => {
            logger.info(`PDF generat: ${outputPath} (${titles.length} títols)`);
            resolve();
        });
        
        stream.on('error', reject);
    });
}

// Consulta 1: Preguntes amb ViewCount > mitjana de ViewCounts
async function queryAboveAverageViewCount(collection) {
    logger.info('\n--- Consulta 1: Preguntes amb ViewCount superior a la mitjana ---');
    
    // Calcular la mitjana
    const avgResult = await collection.aggregate([
        {
            $group: {
                _id: null,
                averageViewCount: { $avg: { $toInt: "$question.ViewCount" } }
            }
        }
    ]).toArray();
    
    const averageViewCount = avgResult[0]?.averageViewCount || 0;
    logger.info(`Mitjana de ViewCount: ${averageViewCount.toFixed(2)}`);
    
    // Cercar preguntes amb ViewCount > mitjana
    const questions = await collection.find({
        "question.ViewCount": { $gt: averageViewCount.toString() }
    }).toArray();
    
    logger.info(`Preguntes trobades: ${questions.length}`);
    
    // Extreure títols
    const titles = questions.map(q => q.question.Title || 'Sense títol');
    
    return titles;
}

// Consulta 2: Preguntes que contenen paraules específiques al títol
async function queryTitlesWithKeywords(collection) {
    logger.info('\n--- Consulta 2: Preguntes amb paraules clau al títol ---');
    logger.info(`Paraules a cercar: ${TITLE_KEYWORDS.join(', ')}`);
    
    // Construir regex per cada paraula
    const orConditions = TITLE_KEYWORDS.map(keyword => ({
        "question.Title": { $regex: keyword, $options: 'i' }
    }));
    
    // Trobar preguntes que compleixin alguna de les condicions regex
    const questions = await collection.find({
        $or: orConditions
    }).toArray();
    
    logger.info(`Preguntes trobades: ${questions.length}`);
    
    // Registrar algun exemple en el log
    if (questions.length > 0) {
        logger.info('Exemples de títols trobats:');
        questions.slice(0, 5).forEach(q => {
            logger.info(`  - ${q.question.Title}`);
        });
        if (questions.length > 5) logger.info(`  ... i ${questions.length - 5} més`);
    }
    
    // Extreure títols
    const titles = questions.map(q => q.question.Title || 'Sense títol');
    
    return titles;
}

// Funció principal
async function main() {
    const client = new MongoClient(MONGODB_URI);
    
    try {
        await client.connect();
        logger.info('Connectat a MongoDB');
        
        const db = client.db(DB_NAME);
        const collection = db.collection(COLLECTION_NAME);
        
        // Verificar que hi ha dades
        const totalCount = await collection.countDocuments();
        logger.info(`Total de preguntes a la col·lecció: ${totalCount}`);
        
        if (totalCount === 0) {
            console.warn('!!! La col·lecció està buida. Executa primer exercici1.js per carregar dades.');
            return;
        }
        
        // Executar consultes
        const titlesAboveAvg = await queryAboveAverageViewCount(collection);
        const titlesWithKeywords = await queryTitlesWithKeywords(collection);
        
        // Generar PDFs
        logger.info('\n--- Generant PDFs ---');
        
        await generatePDF(
            titlesAboveAvg,
            path.join(OUTPUT_DIR, 'informe1.pdf'),
            'Informe 1: Preguntes amb ViewCount superior a la mitjana'
        );
        
        await generatePDF(
            titlesWithKeywords,
            path.join(OUTPUT_DIR, 'informe2.pdf'),
            `Informe 2: Preguntes que contenen al títol: ${TITLE_KEYWORDS.join(', ')}`
        );
        
        logger.info('\n--- Resum final ---');
        logger.info(`>>> Informe 1: ${titlesAboveAvg.length} preguntes → ./data/out/informe1.pdf`);
        logger.info(`>>> Informe 2: ${titlesWithKeywords.length} preguntes → ./data/out/informe2.pdf`);
        
    } catch (error) {
        console.error('Error:', error);
    } finally {
        await client.close();
        logger.info('\nConnexió a MongoDB tancada');
    }
}

// Executar
main();