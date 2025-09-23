package com.project;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.project.utilitats.UtilsCSV;
import com.project.excepcions.IOFitxerExcepcio;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PR123mainTreballadorsTest {

    @TempDir
    File directoriTemporal;

    @Test
    void testModificacioTreballador() throws IOException, IOFitxerExcepcio {
        // Fitxer temporal
        File fitxerTemporal = new File(directoriTemporal, "PR123treballadors.csv");

        // Contingut inicial del CSV
        String contingutInicial = "Id,Nom,Cognom,Departament,Salari\n" +
                                  "123,Nicolás,Rana,2,1000.00\n" +
                                  "435,Xavi,Gil,2,1800.50\n" +
                                  "876,Daniel,Ramos,6,700.30\n" +
                                  "285,Pedro,Drake,4,2500.00\n" +
                                  "224,Joan,Potter,6,1000.00\n";

        // Escriure contingut inicial
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fitxerTemporal))) {
            writer.write(contingutInicial);
        }

        // Crear instància de PR123mainTreballadors
        PR123mainTreballadors gestorTreballadors = new PR123mainTreballadors();
        gestorTreballadors.setFilePath(fitxerTemporal.getAbsolutePath());  // Establir el camí del fitxer temporal

        // Modificar el salari del treballador amb Id 123
        gestorTreballadors.modificarTreballador("123", "Salari", "1200.00");

        // Tornar a llegir el fitxer modificat
        List<String> treballadorsDespres = UtilsCSV.llegir(fitxerTemporal.getAbsolutePath());

        // Comprovar que el fitxer modificat conté el nou salari
        int numLinia = UtilsCSV.obtenirNumLinia(treballadorsDespres, "Id", "123");
        String[] dadesModificades = UtilsCSV.obtenirArrayLinia(treballadorsDespres.get(numLinia));
        assertEquals("1200.00", dadesModificades[4]);
    }
}
