package com.project;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.project.dao.Manager;
import com.project.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

// Tests per a cada mètode de Manager
public class Tests {

    @BeforeAll
    public static void setup() {
        Manager.createSessionFactory();
    }

    @BeforeEach
    public void cleanDatabase() {
        // L'ordre és important per respectar les FK
        try {
            // 1. Eliminar taula pont
            Manager.queryUpdate("DELETE FROM llibre_autor");

            // 2. Eliminar taules amb FK
            // Préstecs (depèn de Persona i Exemplar)
            Manager.queryUpdate("DELETE FROM Prestec");
            
            // Exemplars (depèn de Llibre i Biblioteca)
            Manager.queryUpdate("DELETE FROM Exemplar");
            
            // 3. Eliminar les altres taules
            Manager.queryUpdate("DELETE FROM Llibre");
            Manager.queryUpdate("DELETE FROM Persona");
            Manager.queryUpdate("DELETE FROM Autor");
            Manager.queryUpdate("DELETE FROM Biblioteca");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        Manager.close();
    }
    
    @Test
    public void testAddAutor() {
        Autor autor = Manager.addAutor("Test Autor");
        
        assertNotNull(autor, "L'autor no hauria de ser null");
        assertNotNull(autor.getAutorId(), "L'ID de l'autor hauria d'estar generada");
        assertEquals("Test Autor", autor.getNom(), "El nom de l'autor no coincideix");
    }

    @Test
    public void testAddLlibre() {
        Llibre llibre = Manager.addLlibre("TEST-ISBN", "Test Llibre", "Test Editorial", 2026);
        
        assertNotNull(llibre, "El llibre no hauria de ser null");
        assertNotNull(llibre.getLlibreId(), "L'ID del llibre hauria d'estar generada");
        assertEquals("TEST-ISBN", llibre.getIsbn(), "L'ISBN no coincideix");
        assertEquals("Test Llibre", llibre.getTitol(), "El títol no coincideix");
        assertEquals("Test Editorial", llibre.getEditorial(), "L'editorial no coincideix");
        assertEquals(2026, llibre.getAnyPublicacio(), "L'any de publicació no coincideix");
    }

    @Test
    public void testUpdateAutorAmbLlibres_findLlibresAmbAutors() {
        // Crear autor i llibres
        Autor autor = Manager.addAutor("Autor Original");
        Llibre llibre1 = Manager.addLlibre("ISBN-1", "Llibre 1", "Editorial X", 2024);
        Llibre llibre2 = Manager.addLlibre("ISBN-2", "Llibre 2", "Editorial Y", 2025);
        
        Set<Llibre> llibresSet = new HashSet<>();
        llibresSet.add(llibre1);
        llibresSet.add(llibre2);

        // Actualitzar autor amb nous llibres
        Manager.updateAutor(autor.getAutorId(), "Autor Actualitzat", llibresSet);

        // Verificar actualització de l'autor
        Collection<Autor> autors = Manager.listCollection(Autor.class);
        Autor autorActualitzat = autors.stream()
            .filter(a -> a.getAutorId().equals(autor.getAutorId()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(autorActualitzat, "L'autor hauria d'existir");
        assertEquals("Autor Actualitzat", autorActualitzat.getNom(), "El nom no s'ha actualitzat correctament");
        

        // Verificar que els llibres estan associats a l'autor
        
        // Utilitzar findLlibresAmbAutors() per obtenir els llibres amb autors carregats, 
        // així evitant LazyInitializationException
        List<Llibre> resultats = Manager.findLlibresAmbAutors();
        
        assertNotNull(resultats, "La llista de resultats no hauria de ser null");
        assertFalse(resultats.isEmpty(), "Hi hauria d'haver almenys un resultat");

        boolean llibresAssociats = resultats.stream()
        .filter(l -> l.getLlibreId().equals(llibre1.getLlibreId()) || 
                     l.getLlibreId().equals(llibre2.getLlibreId()))
        .allMatch(l -> !l.getAutors().isEmpty());
    
        assertTrue(llibresAssociats, "Els llibres haurien de tenir autors associats");
    }

    @Test
    public void testAddBiblioteca() {
        Biblioteca biblioteca = Manager.addBiblioteca(
            "Test Biblioteca",
            "Test City", 
            "Test Address",
            "123456789",
            "test@biblioteca.com"
        );

        assertNotNull(biblioteca, "La biblioteca no hauria de ser null");
        assertNotNull(biblioteca.getBibliotecaId(), "L'ID de la biblioteca hauria d'estar generada");
        assertEquals("Test Biblioteca", biblioteca.getNom(), "El nom no coincideix");
        assertEquals("Test City", biblioteca.getCiutat(), "La ciutat no coincideix");
        assertEquals("Test Address", biblioteca.getAdreca(), "L'adreça no coincideix");
        assertEquals("123456789", biblioteca.getTelefon(), "El telèfon no coincideix");
        assertEquals("test@biblioteca.com", biblioteca.getEmail(), "L'email no coincideix");
    }
    
    @Test
    public void testAddExemplar() {
        Llibre llibre = Manager.addLlibre("TEST-ISBN", "Test Llibre", "Test Editorial", 2026);
        Biblioteca biblioteca = Manager.addBiblioteca("Test Biblioteca", "City", "Address", "123456789", "email@test.com");
        
        Exemplar exemplar = Manager.addExemplar("TEST-BARCODE-001", llibre, biblioteca);
        
        assertNotNull(exemplar, "L'exemplar no hauria de ser null");
        assertNotNull(exemplar.getExemplarId(), "L'ID de l'exemplar hauria d'estar generada");
        assertEquals("TEST-BARCODE-001", exemplar.getCodiBarres(), "El codi de barres no coincideix");
        assertTrue(exemplar.isDisponible(), "L'exemplar hauria d'estar disponible per defecte");
        assertEquals(llibre.getLlibreId(), exemplar.getLlibre().getLlibreId(), "El llibre associat no coincideix");
        assertEquals(biblioteca.getBibliotecaId(), exemplar.getBiblioteca().getBibliotecaId(), "La biblioteca associada no coincideix");
    }

    @Test
    public void testAddPersona() {
        Persona persona = Manager.addPersona("12345678Z", "Test Persona", "666777888", "persona@test.com");
        
        assertNotNull(persona, "La persona no hauria de ser null");
        assertNotNull(persona.getPersonaId(), "L'ID de la persona hauria d'estar generada");
        assertEquals("12345678Z", persona.getDni(), "El DNI no coincideix");
        assertEquals("Test Persona", persona.getNom(), "El nom no coincideix");
        assertEquals("666777888", persona.getTelefon(), "El telèfon no coincideix");
        assertEquals("persona@test.com", persona.getEmail(), "L'email no coincideix");
    }

    @Test
    public void testAddPrestec_registrarRetorn() {
        Llibre llibre = Manager.addLlibre("TEST-ISBN", "Test Llibre", "Test Editorial", 2026);
        Biblioteca biblioteca = Manager.addBiblioteca("Test Biblioteca", "City", "Address", "123456789", "email@test.com");
        Exemplar exemplar = Manager.addExemplar("TEST-BARCODE", llibre, biblioteca);
        Persona persona = Manager.addPersona("12345678Z", "Test Persona", "666777888", "persona@test.com");
        
        LocalDate avui = LocalDate.now();
        Prestec primerPrestec = Manager.addPrestec(exemplar, persona, avui, avui.plusDays(15));

        assertNotNull(primerPrestec, "El préstec hauria de crear-se correctament");
        assertTrue(primerPrestec.isActiu(), "El préstec hauria d'estar actiu");
        assertEquals(avui, primerPrestec.getDataPrestec(), "La data de préstec no coincideix");
        assertEquals(avui.plusDays(15), primerPrestec.getDataRetornPrevista(), "La data de retorn prevista no coincideix");
        assertNull(primerPrestec.getDataRetornReal(), "La data de retorn real hauria de ser null inicialment");

        // Verificar que l'exemplar ja no està disponible
        Collection<Exemplar> exemplars = Manager.listCollection(Exemplar.class);
        Exemplar exemplarActualitzat = exemplars.stream()
            .filter(e -> e.getExemplarId().equals(exemplar.getExemplarId()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(exemplarActualitzat, "L'exemplar hauria d'existir");
        assertFalse(exemplarActualitzat.isDisponible(), "L'exemplar hauria d'estar no disponible després del préstec");

        // Segon préstec amb el mateix exemplar (hauria de fallar)
        Prestec segonPrestec = Manager.addPrestec(exemplar, persona, avui, avui.plusDays(20));
        assertNull(segonPrestec, "El segon préstec hauria de retornar null perquè l'exemplar no està disponible");



        // Registrar retorn
        LocalDate dataRetornReal = avui.plusDays(7);
        Manager.registrarRetornPrestec(primerPrestec.getPrestecId(), dataRetornReal);

        // Verificar actualització del préstec
         Collection<Prestec> prestecs = Manager.listCollection(Prestec.class);
        Prestec prestecActualitzat = prestecs.stream()
            .filter(p -> p.getPrestecId().equals(primerPrestec.getPrestecId()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(prestecActualitzat, "El préstec hauria d'existir");
        assertFalse(prestecActualitzat.isActiu(), "El préstec hauria d'estar inactiu després del retorn");
        assertEquals(dataRetornReal, prestecActualitzat.getDataRetornReal(), "La data de retorn real no coincideix");

        // Verificar que l'exemplar torna a estar disponible
        exemplars = Manager.listCollection(Exemplar.class);
        exemplarActualitzat = exemplars.stream()
            .filter(e -> e.getExemplarId().equals(exemplar.getExemplarId()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(exemplarActualitzat, "L'exemplar hauria d'existir");
        assertTrue(exemplarActualitzat.isDisponible(), "L'exemplar hauria d'estar disponible després del retorn");
    }

    @Test
    public void testFindLlibresEnPrestec() {
        Llibre llibre = Manager.addLlibre("TEST-ISBN", "Test Llibre", "Test Editorial", 2026);
        Biblioteca biblioteca = Manager.addBiblioteca("Test Biblioteca", "City", "Address", "123456789", "email@test.com");
        Exemplar exemplar = Manager.addExemplar("TEST-BARCODE", llibre, biblioteca);
        Persona persona = Manager.addPersona("12345678Z", "Test Persona", "666777888", "persona@test.com");
        
        LocalDate avui = LocalDate.now();
        Manager.addPrestec(exemplar, persona, avui, avui.plusDays(15));
        
        // Executar consulta
        List<Object[]> resultats = Manager.findLlibresEnPrestec();
        
        assertNotNull(resultats, "La llista de resultats no hauria de ser null");
        assertFalse(resultats.isEmpty(), "Hi hauria d'haver almenys un resultat");
        
        // Verificar estructura del resultat
        Object[] primerResultat = resultats.get(0);
        assertEquals(2, primerResultat.length, "Cada resultat hauria de tenir 2 elements");
        assertNotNull(primerResultat[0], "El títol del llibre no hauria de ser null");
        assertNotNull(primerResultat[1], "El nom de la persona no hauria de ser null");
    }

    @Test
    public void testFindLlibresAmbBiblioteques() {
        // Crear dades de prova
        Llibre llibre = Manager.addLlibre("TEST-ISBN", "Test Llibre", "Test Editorial", 2024);
        Biblioteca biblioteca = Manager.addBiblioteca("Test Biblio", "City", "Address", "123", "email@test.com");
        Manager.addExemplar("TEST-BARCODE", llibre, biblioteca);
        
        // Executar consulta
        List<Object[]> resultats = Manager.findLlibresAmbBiblioteques();
        
        assertNotNull(resultats, "La llista de resultats no hauria de ser null");
        assertFalse(resultats.isEmpty(), "Hi hauria d'haver almenys un resultat");
        
        // Verificar estructura del resultat
        Object[] primerResultat = resultats.get(0);
        assertEquals(2, primerResultat.length, "Cada resultat hauria de tenir 2 elements");
        assertNotNull(primerResultat[0], "El títol del llibre no hauria de ser null");
        assertNotNull(primerResultat[1], "El nom de la biblioteca no hauria de ser null");
    }
}
