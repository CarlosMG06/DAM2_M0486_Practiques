const fs = require('fs');
const path = require('path');
const winston = require('winston'); // logger
const { format } = winston; 

// Assegurar que el directori de logs existeix
const logDir = path.join(__dirname, '../../data/logs');
if (!fs.existsSync(logDir)) {
  fs.mkdirSync(logDir, { recursive: true });
}

// Format comú per tots els transports (copiat de PR4.2 xat-api)
const logFormat = format.combine(
    format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
    format.errors({ stack: true }),
    format.printf(({ level, message, timestamp, stack, ...metadata }) => {
        let log = `${timestamp} ${level}: ${message}`;
        
        // Afegir metadata si existeix
        if (Object.keys(metadata).length > 0) {
            log += ` ${JSON.stringify(metadata)}`;
        }
        
        // Afegir stack trace si existeix
        if (stack) {
            log += `\n${stack}`;
        }
        
        return log;
    })
);

// Configurar logger
const logger = winston.createLogger({
  level: 'info',
  format: logFormat,
  transports: [
    // Log a fitxer
    new winston.transports.File({ 
      filename: path.join(logDir, 'exercici1.log'),
      maxsize: '20m',
      maxFiles: 5
    }),
    // Log a consola
    new winston.transports.Console({
        format: format.combine(
            format.colorize(),
            logFormat
        )
    })
  ]
});

module.exports = logger;