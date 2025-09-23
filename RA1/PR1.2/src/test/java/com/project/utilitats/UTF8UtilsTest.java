package com.project.utilitats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Classe de test exhaustiva per a la utilitat UTF8Utils.
 * Prova el mètode 'truncar' en diversos escenaris per assegurar
 * que trunca correctament arrays de bytes UTF-8 sense corrompre caràcters.
 */
@DisplayName("Tests per a la classe UTF8Utils")
class UTF8UtilsTest {

    // Helper per convertir String a byte[]
    private byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("No ha de truncar si el text és més curt que el límit")
    void testNoTruncationNeeded() {
        byte[] original = bytes("Hola");
        byte[] resultat = UTF8Utils.truncar(original, 10);
        assertArrayEquals(original, resultat, "El text curt no hauria de ser modificat.");
    }

    @Test
    @DisplayName("No ha de truncar si el text té exactament la mida del límit")
    void testExactLimitSize() {
        byte[] original = bytes("TestExacte"); // 10 bytes
        byte[] resultat = UTF8Utils.truncar(original, 10);
        assertArrayEquals(original, resultat, "El text amb la mida exacta del límit no hauria de ser modificat.");
    }

    @Test
    @DisplayName("Ha de truncar correctament un text simple (només ASCII)")
    void testSimpleAsciiTruncation() {
        byte[] original = bytes("Hello World");
        byte[] esperat = bytes("Hello");
        byte[] resultat = UTF8Utils.truncar(original, 5);
        assertArrayEquals(esperat, resultat, "El text ASCII hauria de ser truncat al límit exacte.");
    }

    @Test
    @DisplayName("Ha de truncar just abans d'un caràcter multi-byte si el límit cau al mig")
    void testMultiByteTruncationMidCharacter() {
        // 'J' (1 byte), 'ú' (2 bytes), 'l' (1 byte), 'i' (1 byte), 'a' (1 byte) -> Total 6 bytes
        byte[] original = bytes("Júlia");
        byte[] esperat = bytes("J"); // El límit 2 cau al mig de 'ú', per tant 'ú' s'ha d'eliminar.
        byte[] resultat = UTF8Utils.truncar(original, 2);
        assertArrayEquals(esperat, resultat, "Hauria de truncar abans del caràcter multi-byte incomplet.");
    }
    
    @Test
    @DisplayName("Ha d'incloure el caràcter multi-byte si el límit ho permet completament")
    void testMultiByteTruncationAfterCharacter() {
        byte[] original = bytes("Júlia");
        byte[] esperat = bytes("Jú"); // 'J' (1) + 'ú' (2) = 3 bytes. El límit és 3.
        byte[] resultat = UTF8Utils.truncar(original, 3);
        assertArrayEquals(esperat, resultat, "Hauria d'incloure el caràcter multi-byte si hi cap sencer.");
    }

    @Test
    @DisplayName("Prova amb caràcters asiàtics (3 bytes per caràcter)")
    void testAsianCharactersTruncation() {
        // Cada caràcter ocupa 3 bytes. "你好世界" -> 9 bytes
        byte[] original = bytes("你好世界");
        byte[] esperat = bytes("你好"); // 6 bytes
        byte[] resultat = UTF8Utils.truncar(original, 8); // El límit 8 cau al mig del 3r caràcter.
        assertArrayEquals(esperat, resultat, "Hauria de truncar abans del caràcter asiàtic incomplet.");
    }

    @Test
    @DisplayName("Prova amb emojis (4 bytes per caràcter)")
    void testEmojiTruncation() {
        // "Test" (4 bytes) + "🚀" (4 bytes) -> Total 8 bytes
        byte[] original = bytes("Test🚀");
        byte[] esperat = bytes("Test"); // El límit 7 cau al mig de l'emoji.
        byte[] resultat = UTF8Utils.truncar(original, 7);
        assertArrayEquals(esperat, resultat, "Hauria de truncar abans de l'emoji incomplet.");
    }

    @Test
    @DisplayName("Ha de retornar un array buit si l'entrada és buida")
    void testEmptyInput() {
        byte[] original = bytes("");
        byte[] esperat = bytes("");
        byte[] resultat = UTF8Utils.truncar(original, 10);
        assertArrayEquals(esperat, resultat, "Una entrada buida ha de retornar una sortida buida.");
    }

    @Test
    @DisplayName("Ha de retornar un array buit si el límit és 0")
    void testZeroLimit() {
        byte[] original = bytes("No buit");
        byte[] esperat = bytes("");
        byte[] resultat = UTF8Utils.truncar(original, 0);
        assertArrayEquals(esperat, resultat, "Un límit de 0 ha de retornar un array buit.");
    }
    
    @Test
    @DisplayName("Ha de retornar un array buit si el límit és negatiu")
    void testNegativeLimit() {
        byte[] original = bytes("No buit");
        byte[] esperat = bytes("");
        byte[] resultat = UTF8Utils.truncar(original, -5);
        assertArrayEquals(esperat, resultat, "Un límit negatiu ha de retornar un array buit.");
    }

@Test
    @DisplayName("No ha de fer res si el límit és més gran que la longitud de l'array")
    void testLimitExceedsArrayLength() {
        byte[] original = bytes("Test"); // 4 bytes
        byte[] esperat = bytes("Test");
        
        // Ara comprovem que el resultat és idèntic a l'original, sense esperar cap excepció.
        byte[] resultat = UTF8Utils.truncar(original, 100);
        
        assertArrayEquals(esperat, resultat, 
            "Si el límit és més gran, hauria de retornar l'array original sense canvis.");
    }
}