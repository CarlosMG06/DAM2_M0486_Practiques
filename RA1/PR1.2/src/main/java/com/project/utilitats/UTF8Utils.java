package com.project.utilitats;

public class UTF8Utils {

    /**
     * Trunca un array de bytes UTF-8 a un nombre màxim de bytes sense tallar caràcters a la meitat.
     *
     * @param nomBytes L'array de bytes original.
     * @param limit El nombre màxim de bytes permesos.
     * @return Un nou array de bytes truncat correctament.
     */
    public static byte[] truncar(byte[] nomBytes, int limit) {
        // 1. Gestionem els casos límit per evitar errors i millorar l'eficiència.
        if (nomBytes == null || nomBytes.length == 0 || limit <= 0) {
            return new byte[0];
        }

        // 2. Si no cal truncar, retornem l'array original directament.
        if (nomBytes.length <= limit) {
            return nomBytes;
        }

        int validBytesCount = 0;

        // 3. Iterem mentre tinguem bytes per processar.
        while (validBytesCount < nomBytes.length) {
            // Determinem la mida del següent caràcter.
            int charSize = getCharSize(nomBytes[validBytesCount]);
            
            // 4. La lògica clau: comprovem SI EL SEGÜENT CARÀCTER HI CABRÀ.
            if (validBytesCount + charSize > limit) {
                // Si afegir aquest caràcter supera el límit, ens aturem aquí.
                break;
            }
            
            // Si hi cap, l'incloem incrementant el comptador.
            validBytesCount += charSize;
        }

        // 5. Creem el nou array amb la mida exacta i copiem els bytes vàlids.
        byte[] nomTruncat = new byte[validBytesCount];
        System.arraycopy(nomBytes, 0, nomTruncat, 0, validBytesCount);
        return nomTruncat;
    }

    /**
     * Funció auxiliar per determinar la mida en bytes d'un caràcter UTF-8
     * a partir del seu primer byte.
     */
    private static int getCharSize(byte startByte) {
        if ((startByte & 0x80) == 0) {      // 1-byte char (0xxxxxxx)
            return 1;
        } else if ((startByte & 0xE0) == 0xC0) { // 2-byte char (110xxxxx)
            return 2;
        } else if ((startByte & 0xF0) == 0xE0) { // 3-byte char (1110xxxx)
            return 3;
        } else if ((startByte & 0xF8) == 0xF0) { // 4-byte char (11110xxx)
            return 4;
        }
        // Si és un byte de continuació (10xxxxxx), es tracta com a part d'un caràcter invàlid.
        // Retornem 1 per poder avançar i evitar bucles infinits, tot i que no hauria de passar.
        return 1;
    }
}