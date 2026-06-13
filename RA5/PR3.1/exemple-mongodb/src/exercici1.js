const path = require('path');
const entities = require('entities');

const { loadDataToMongoDB } = require('./load-xml-to-mongodb');
const logger = require('./logger');

// Decodificar entitats HTML
function decodeHtmlEntities(text) {
  if (!text || typeof text !== 'string') return text;
  return entities.decodeHTML(text);
}

// Processar row
function processRow(row) {
  const question = {};
  
  for (const [key, value] of Object.entries(row)) {
    // Decodificar els camps que poden contenir HTML
    if (key === 'Body' || key === 'Title' || key === 'Tags') {
      question[key] = decodeHtmlEntities(value);
    } else {
      question[key] = value;
    }
  }

  return { question };
}

// Processar les dades de Stack Exchange
function processStackExchangeData(data) {
  logger.info('Inici processament de dades de Stack Exchange');
 
  const rows = Array.isArray(data.posts.row) 
    ? data.posts.row 
    : [data.posts.row];
  
  logger.info(`Total d'elements al XML: ${rows.length}`);
  
  // Filtrar només preguntes (PostTypeId = "1")
  const questions = rows
    .filter(row => row.PostTypeId === '1')
    .map(row => processRow(row));
  
  logger.info(`Preguntes trobades: ${questions.length}`);
  
  // Ordenar per ViewCount (de major a menor)
  logger.info('Ordenant preguntes per visites...');
  const sortedQuestions = questions.sort((a, b) => {
    return parseInt(b.question.ViewCount) - parseInt(a.question.ViewCount);
  });
  
  // Seleccionar les 10.000 primeres
  const top10000 = sortedQuestions.slice(0, 10000);
  
  if (top10000.length > 0) {
    logger.info(`Top 10.000 preguntes seleccionades:`);
    logger.info(`- Mínim de visites: ${top10000[top10000.length-1].question.ViewCount}`);
    logger.info(`- Màxim de visites: ${top10000[0].question.ViewCount}`);
  }

  return top10000;
}

const stackExchangeXmlPath = path.join(__dirname, '../../data/boardgames.stackexchange.com/Posts.xml');

async function main() {
  logger.info('=== EXERCICI1: Càrrega de les 10.000 preguntes més vistes ===');
  
  try {
    await loadDataToMongoDB(
      stackExchangeXmlPath,
      processStackExchangeData,
      'stackexchange_db',
      'questions'
    );
    logger.info('=== EXERCICI1 COMPLETAT ===');
  } catch (error) {
    logger.error(`ERROR: ${error.message}`);
    logger.error(error.stack);
    process.exit(1);
  }
}

main();