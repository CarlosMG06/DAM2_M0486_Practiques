package com.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Manager {

    private static SessionFactory factory;

    public static void createSessionFactory() {
        createSessionFactory("hibernate.properties");
    }

    public static void createSessionFactory(String propertiesFileName) {
        try {
            // CONFIGURATION: Configura Hibernate programàticament
            Configuration configuration = new Configuration();
            
            // Registrem les classes @Entity que Hibernate ha de gestionar
            configuration.addAnnotatedClass(Ciutat.class);
            configuration.addAnnotatedClass(Ciutada.class);

            // Carreguem les propietats des del fitxer (URL BBDD, usuari, contrasenya...)
            Properties properties = new Properties();
            try (InputStream input = Manager.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                if (input == null) {
                    throw new IOException("No s'ha pogut trobar " + propertiesFileName);
                }
                properties.load(input);
            }
            configuration.addProperties(properties);
            
            // SERVICE REGISTRY: Gestiona els serveis interns d'Hibernate
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
                
            // Construïm el SessionFactory (operació costosa, només es fa un cop)
            factory = configuration.buildSessionFactory(serviceRegistry);
            
        } catch (Throwable ex) { 
            System.err.println("Error en crear sessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex); 
        }
    }

    public static void close() {
        if (factory != null) factory.close();
    }

    private static void executeInTransaction(Consumer<Session> action) {
        Transaction tx = null;
        Session session = factory.openSession();
        try {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        } finally {
            session.close();
        }
    }

    private static <T> T executeInTransactionWithResult(Function<Session, T> action) {
        Transaction tx = null;
        Session session = factory.openSession();
        try {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        } finally {
            session.close();
        }
    }

    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        return executeInTransactionWithResult(session -> {
            Ciutat ciutat = new Ciutat(nom, pais, poblacio);
            session.persist(ciutat);
            return ciutat;
        });
    }
    public static Ciutada addCiutada(String nom, String cognom, int edat) {
        return executeInTransactionWithResult(session -> {
            Ciutada ciutada = new Ciutada(nom, cognom, edat);
            session.persist(ciutada);
            return ciutada;
        });
    }

    public static void updateCiutat(long ciutatId, String nom, String pais, int poblacio, Set<Ciutada> newCiutadans) {
        executeInTransaction(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat == null) return;
            
            ciutat.setNom(nom);
            ciutat.setPais(pais);
            ciutat.setPoblacio(poblacio);
            
            if (newCiutadans != null) {

                // 1. Eliminar ciutadans que ja no estan a la nova llista
                Set<Ciutada> currentCiutadans = new HashSet<>(ciutat.getCiutadans());
                for (Ciutada dbCiutada : currentCiutadans) {
                    if (!newCiutadans.contains(dbCiutada)) {
                        ciutat.removeCiutada(dbCiutada);
                    }
                }

                // 2. Afegir o actualitzar ciutadans de la nova llista
                for (Ciutada ciutada : newCiutadans) {
                    if (ciutada.getCiutadaId() != null) {
                        // FIND: Recupera l'entitat "managed" (gestionada per la sessió)
                        // Evita errors de "detached entity" quan l'objecte ve de fora la sessió
                        Ciutada managedCiutada = session.find(Ciutada.class, ciutada.getCiutadaId());
                        if (managedCiutada != null && !ciutat.getCiutadans().contains(managedCiutada)) {
                            ciutat.addCiutada(managedCiutada);
                        }
                    } else {
                        // Ciutada nou sense ID: s'afegeix i es persistirà per CASCADE
                        ciutat.addCiutada(ciutada);
                    }
                }
            } else {
                // Si newCiutadans és null, eliminem tots els ciutadans de la ciutat
                new HashSet<>(ciutat.getCiutadans()).forEach(ciutat::removeCiutada);
            }

            session.merge(ciutat);
        });
    }
    public static void updateCiutada(long ciutadaId, String nom, String cognom, int edat) {
        executeInTransaction(session -> {
            Ciutada ciutada = session.get(Ciutada.class, ciutadaId);
            if (ciutada != null) {
                ciutada.setNom(nom);
                ciutada.setCognom(cognom);
                ciutada.setEdat(edat);
                session.merge(ciutada);
            }
        });
    }

    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        return executeInTransactionWithResult(session -> {
            String hql = "SELECT c FROM Ciutat c LEFT JOIN FETCH c.ciutadans WHERE c.ciutatId = :id";
            return session.createQuery(hql, Ciutat.class)
                          .setParameter("id", ciutatId)
                          .uniqueResult();
        });
    }

    public static <T> T getById(Class<T> clazz, long id) {
        return executeInTransactionWithResult(session -> session.get(clazz, id));
    }

    public static <T> void delete(Class<T> clazz, Serializable id) {
        executeInTransaction(session -> {
            T obj = session.get(clazz, id);
            if (obj != null) {
                session.remove(obj);
            }
        });
    }

    public static <T> List<T> findAll(Class<T> clazz, String whereClause) {
        return executeInTransactionWithResult(session -> {
            String hql = "FROM " + clazz.getName();
            if (whereClause != null && !whereClause.trim().isEmpty()) {
                hql += " WHERE " + whereClause;
            }
            return session.createQuery(hql, clazz).list();
        });
    }
    public static <T> List<T> findAll(Class<T> clazz) {
        return findAll(clazz, "");
    }

    public static List<Ciutat> findAllCiutatsWithCiutadans() {
        return executeInTransactionWithResult(session -> {
            // DISTINCT: Evita duplicats del Ciutat pare quan té múltiples Ciutadans
            // (el JOIN multiplica files: 1 Ciutat amb 3 Ciutadans = 3 files)
            return session.createQuery(
                "SELECT DISTINCT c FROM Ciutat c LEFT JOIN FETCH c.ciutadans", 
                Ciutat.class
            ).list();
        });
    }

    public static <T> String collectionToString(Class<T> clazz, Collection<T> collection) {
        if (collection == null || collection.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        for (T obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }


}