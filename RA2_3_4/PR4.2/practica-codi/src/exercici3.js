// Importacions
const fs = require('fs').promises;
const path = require('path');
require('dotenv').config();

// Constants des de variables d'entorn
const IMAGES_SUBFOLDER = 'imatges/animals';
const IMAGE_TYPES = ['.jpg', '.jpeg', '.png', '.gif'];
const OLLAMA_URL = process.env.CHAT_API_OLLAMA_URL;
const OLLAMA_MODEL = process.env.CHAT_API_OLLAMA_MODEL_VISION;
const OUTPUT_FILE_NAME = 'exercici3_resposta.json';

// Funció per llegir un fitxer i convertir-lo a Base64
async function imageToBase64(imagePath) {
    try {
        const data = await fs.readFile(imagePath);
        return Buffer.from(data).toString('base64');
    } catch (error) {
        console.error(`Error al llegir o convertir la imatge ${imagePath}:`, error.message);
        return null;
    }
}

// Funció per fer la petició a Ollama amb més detalls d'error
async function queryOllama(base64Image, prompt) {
    const requestBody = {
        model: OLLAMA_MODEL,
        prompt: prompt,
        images: [base64Image],
        stream: false
    };

    try {
        console.log('Enviant petició a Ollama...');
        console.log(`URL: ${OLLAMA_URL}/generate`);
        console.log('Model:', OLLAMA_MODEL);
        
        const response = await fetch(`${OLLAMA_URL}/generate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        });

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();
        
        // Depuració de la resposta
        console.log('Resposta completa d\'Ollama:', JSON.stringify(data, null, 2));
        
        // Verificar si tenim una resposta vàlida
        if (!data || !data.response) {
            throw new Error('La resposta d\'Ollama no té el format esperat');
        }

        return data.response;
    } catch (error) {
        console.error('Error detallat en la petició a Ollama:', error);
        console.error('Detalls adicionals:', {
            url: `${OLLAMA_URL}/generate`,
            model: OLLAMA_MODEL,
            promptLength: prompt.length,
            imageLength: base64Image.length
        });
        return null;
    }
}

// Funció principal
async function main() {
    try {
        // Validem les variables d'entorn necessàries
        if (!process.env.DATA_PATH) {
            throw new Error('La variable d\'entorn DATA_PATH no està definida.');
        }
        if (!OLLAMA_URL) {
            throw new Error('La variable d\'entorn CHAT_API_OLLAMA_URL no està definida.');
        }
        if (!OLLAMA_MODEL) {
            throw new Error('La variable d\'entorn CHAT_API_OLLAMA_MODEL no està definida.');
        }

        const imagesFolderPath = path.join(__dirname, process.env.DATA_PATH, IMAGES_SUBFOLDER);
        try {
            await fs.access(imagesFolderPath);
        } catch (error) {
            throw new Error(`El directori d'imatges no existeix: ${imagesFolderPath}`);
        }

        const animalDirectories = await fs.readdir(imagesFolderPath);

        // Iterem per cada element dins del directori d'animals
        for (const animalDir of animalDirectories) {
            // Construïm la ruta completa al directori de l'animal actual
            const animalDirPath = path.join(imagesFolderPath, animalDir);

            try {
                // Obtenim informació sobre l'element (si és directori, fitxer, etc.)
                const stats = await fs.stat(animalDirPath);
                
                // Si no és un directori, l'ignorem i continuem amb el següent
                if (!stats.isDirectory()) {
                    console.log(`S'ignora l'element no directori: ${animalDirPath}`);
                    continue;
                }
            } catch (error) {
                // Si hi ha error al obtenir la info del directori, el loguegem i continuem
                console.error(`Error al obtenir informació del directori: ${animalDirPath}`, error.message);
                continue;
            }

            // Obtenim la llista de tots els fitxers dins del directori de l'animal
            const imageFiles = await fs.readdir(animalDirPath);

            // Iterem per cada fitxer dins del directori de l'animal
            for (const imageFile of imageFiles) {
                // Construïm la ruta completa al fitxer d'imatge
                const imagePath = path.join(animalDirPath, imageFile);
                // Obtenim l'extensió del fitxer i la convertim a minúscules
                const ext = path.extname(imagePath).toLowerCase();
                
                // Si l'extensió no és d'imatge vàlida (.jpg, .png, etc), l'ignorem
                if (!IMAGE_TYPES.includes(ext)) {
                    console.log(`S'ignora fitxer no vàlid: ${imagePath}`);
                    continue;
                }

                // Convertim la imatge a format Base64 per enviar-la a Ollama
                const base64String = await imageToBase64(imagePath);

                // Si s'ha pogut convertir la imatge correctament
                if (base64String) {
                    // Loguegem informació sobre la imatge que processarem
                    console.log(`\nProcessant imatge: ${imagePath}`);
                    console.log(`Mida de la imatge en Base64: ${base64String.length} caràcters`);
                    
                    // Definim el prompt per a Ollama
                    // const prompt = "Identifica quin tipus d'animal apareix a la imatge";
                    const prompt = `Realitza una anàlisi detallada de l'animal que apareix a la imatge. 
Proporciona la següent informació: 
1. Nom comú 
2. Nom científic ('null' si es desconeix) 
3. Taxonomia
    3a. Classe taxonòmica (mamífer, au, rèptil, amfibi, peix, invertebrat) 
    3b. Ordre taxonòmic 
    3c. Família taxonòmica 
4. Hàbitat natural
    4a. Llista de tipus d'hàbitats 
    4b. Llista de regions geogràfiques 
    4c. Llista de climes
5. Dieta
    5a. Tipus de dieta (carnívor/herbívor/omnívor)
    5b. Llista d'aliments principals
6. Característiques físiques
    6a. Altura mitjana (en cm)
    6b. Pes mitjà (en kg)
    6c. Llista de colors predominants
    6d. Llista de trets físics distintius
7. Estat de conservació
    7a. Classificació segons la IUCN
    7b. Llista d'amenaces principals
Respon en text parsejable com JSON amb les claus: 
nom_comu, nom_cientific, taxonomia {classe, ordre, familia},
habitat {tipus, regio_geografica, climes}, dieta {tipus, aliments_principals},
caracteristiques_fisiques {mida {altura_mitjana_cm, pes_mitja_kg}, colors_predominants, trets_distintius},
estat_conservacio {classificacio_IUCN, amenaces_principals}.
NO incloguis cap altre text addicional dins la teva resposta.` // Ni '\`\`\`json ... \`\`\`' ni altres caràcters. Només les claus interpretables com a JSON.`;
                    console.log('Prompt:', prompt);
                    
                    // Fem la petició a Ollama amb la imatge i el prompt
                    const response = await queryOllama(base64String, prompt);
                    
                    // Processem la resposta d'Ollama
                    if (response) {
                        console.log(`\nResposta d'Ollama per ${imageFile}:`);
                        console.log(response);
                        try {
                            const parsedResponse = JSON.parse(response);

                            // Guardem la resposta en exercici3_resposta.json
                            const outputFilePath = path.join(__dirname, process.env.DATA_PATH, OUTPUT_FILE_NAME);
                            const resultString = JSON.stringify(parsedResponse, null, 2);
                            fs.writeFileSync(outputFilePath, resultString);
                            console.log(`Resposta guardada a ${outputFilePath}`);
                        } catch (e) {
                            console.error('Error en parsejar la resposta JSON:', e.message);
                        }
                    } else {
                        // Si no hem rebut resposta vàlida, loguegem l'error
                        console.error(`\nNo s'ha rebut resposta vàlida per ${imageFile}`);
                    }
                    // Separador per millorar la llegibilitat del output
                    console.log('------------------------');
                }
            }
            console.log(`\nATUREM L'EXECUCIÓ DESPRÉS D'ITERAR EL CONTINGUT DEL PRIMER DIRECTORI`);
            break; // ATUREM L'EXECUCIÓ DESPRÉS D'ITERAR EL CONTINGUT DEL PRIMER DIRECTORI
        }

    } catch (error) {
        console.error('Error durant l\'execució:', error.message);
    }
}

// Executem la funció principal
main();