const { webcrypto } = require('crypto');
if (!globalThis.crypto) {
  globalThis.crypto = webcrypto;
}

const fs = require('fs');
const path = require('path');
const { MongoClient } = require('mongodb');
const xml2js = require('xml2js');
require('dotenv').config();

const logger = require('./logger');

// Ruta al fitxer XML
const xmlFilePath = path.join(__dirname, '../../data/youtubers.xml');

// Funció per llegir i analitzar el fitxer XML
async function parseXMLFile(filePath) {
  try {
    const xmlData = fs.readFileSync(filePath, 'utf-8');
    const parser = new xml2js.Parser({ 
      explicitArray: false,
      mergeAttrs: true
    });
    
    return new Promise((resolve, reject) => {
      parser.parseString(xmlData, (err, result) => {
        if (err) {
          reject(err);
        } else {
          resolve(result);
        }
      });
    });
  } catch (error) {
    console.error('Error llegint o analitzant el fitxer XML:', error);
    throw error;
  }
}

// Funció per processar les dades i transformar-les a un format més adequat per MongoDB
function processYoutuberData(data) {
  const youtubers = Array.isArray(data.youtubers.youtuber) 
    ? data.youtubers.youtuber 
    : [data.youtubers.youtuber];
  
  return youtubers.map(youtuber => {
    // Assegurem que categories i videos siguin arrays
    const categories = Array.isArray(youtuber.categories.category) 
      ? youtuber.categories.category 
      : [youtuber.categories.category];
    
    const videos = Array.isArray(youtuber.videos.video) 
      ? youtuber.videos.video 
      : [youtuber.videos.video];
    
    // Convertim els videos a un format més adequat
    const processedVideos = videos.map(video => ({
      videoId: video.id,
      title: video.title,
      duration: video.duration,
      views: parseInt(video.views),
      uploadDate: new Date(video.uploadDate),
      likes: parseInt(video.likes),
      comments: parseInt(video.comments)
    }));
    
    // Retornem el document processat
    return {
      youtuberId: youtuber.id,
      channel: youtuber.channel,
      name: youtuber.n,
      subscribers: parseInt(youtuber.subscribers),
      joinDate: new Date(youtuber.joinDate),
      categories: categories,
      videos: processedVideos
    };
  });
}

// Funció principal per carregar les dades a MongoDB
async function loadDataToMongoDB(
    xmlPath = xmlFilePath, 
    processData = processYoutuberData, 
    dbName = 'youtubers_db',
    collectionName = 'youtubers'
  ) {
  // Configuració de la connexió a MongoDB
  const uri = process.env.MONGODB_URI || 'mongodb://localhost:27017/';
  const client = new MongoClient(uri);
  
  try {
    await client.connect();
    logger.info('Connectat a MongoDB');
    
    const database = client.db(dbName);
    const collection = database.collection(collectionName);
    
    // Llegir i analitzar el fitxer XML
    logger.info('Llegint el fitxer XML...');
    const xmlData = await parseXMLFile(xmlPath);
    
    // Processar les dades
    logger.info('Processant les dades...');
    const youtubers = processData(xmlData);
    
    // Eliminar dades existents (opcional)
    logger.info('Eliminant dades existents...');
    await collection.deleteMany({});
    
    // Inserir les noves dades
    logger.info('Inserint dades a MongoDB...');
    const result = await collection.insertMany(youtubers);
    
    logger.info(`${result.insertedCount} documents inserits correctament.`);
    logger.info('Dades carregades amb èxit!');
    
  } catch (error) {
    console.error('Error carregant les dades a MongoDB:', error);
  } finally {
    await client.close();
    logger.info('Connexió a MongoDB tancada');
  }
}

// Executar la funció principal
loadDataToMongoDB();

module.exports = {loadDataToMongoDB}