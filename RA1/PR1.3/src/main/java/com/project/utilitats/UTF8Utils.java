package com.project.utilitats;

public class UTF8Utils {
    // Funció per truncar un array de bytes UTF-8 a un nombre màxim de bytes sense tallar caràcters
    public static byte[] truncar(byte[] nomBytes, int limit) {
        int byteCount = 0;
        int lastValidPos = 0;

        // Iterem byte a byte assegurant-nos que no es talli a la meitat d'un caràcter
        for (int i = 0; i < limit; i++) {
            byte currentByte = nomBytes[i];

            // Comprova si és un byte inicial d'un caràcter UTF-8 multi-byte
            if ((currentByte & 0x80) == 0) { // 1 byte character
                lastValidPos = i;
            } else if ((currentByte & 0xE0) == 0xC0) { // 2 bytes character
                lastValidPos = i + 1;
            } else if ((currentByte & 0xF0) == 0xE0) { // 3 bytes character
                lastValidPos = i + 2;
            } else if ((currentByte & 0xF8) == 0xF0) { // 4 bytes character
                lastValidPos = i + 3;
            }

            // Incrementar comptador de bytes processats
            byteCount = lastValidPos + 1;

            // Si sobrepassem el límit de bytes, tallar aquí
            if (byteCount >= limit) break;
        }

        // Retorna només els bytes vàlids fins al límit
        byte[] nomTruncat = new byte[byteCount];
        System.arraycopy(nomBytes, 0, nomTruncat, 0, byteCount);
        return nomTruncat;
    }    
}
