package com.project;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

public class Manager {

    private static SessionFactory factory;

    public void createSessionFactory() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
        }
    }

    public void close() {
        if (factory != null) factory.close();
    }

    private static void executeInTransaction(Consumer<Session> action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        }
    }

    private static <T> T executeInTransactionWithResult(Function<Session, T> action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        }
    }

    public void addCiutat(String nom, String pais, int poblacio) {
        return executeInTransactionWithResult(session -> {
            Ciutat ciutat = new Ciutat(type);
            session.persist(ciutat);
            return ciutat;
        });
    }
    public void addCiutada(String nom, String cognom, int edat) {
        return executeInTransactionWithResult(session -> {
            Ciutada ciutada = new Ciutada(type);
            session.persist(ciutada);
            return ciutada;
        });
    }

    public void updateCiutat(long ciutatId, String nom, String pais, int poblacio, Set<Ciutada> newCiutadans) {
        executeInTransaction(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat == null) return;
            
            ciutat.setNom(nom);
            ciutat.setPais(pais);
            ciutat.setPoblacio(poblacio);
            
            if (newCiutadans != null) {

                // 1. Netejar ciutadans existents
                if (ciutat.getCiutadans() != null && !ciutat.getCiutadans().isEmpty()) {
                    List<Ciutada> ciutadansToRemove = List.copyOf(ciutat.getCiutadans());
                    ciutadansToRemove.forEach(ciutat::removeCiutada);
                }

                // 2. Afegir nous ciutadans (recuperant-los com a "managed")
                for (Ciutada ciutada : newCiutadans) {
                    Ciutada managedCiutada = session.get(Ciutada.class, ciutada.getCiutadaId());
                    if (managedCiutada != null) {
                        ciutat.addCiutada(managedCiutada);
                    }
                }
            }

            session.merge(ciutat);
        });
    }
    public void updateCiutada(int ciutadaId, String nom, String cognom, int edat) {
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

    public Ciutat getCiutatWithCiutadans(int ciutatId) {
        return executeInTransactionWithResult(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat != null) {
                Hibernate.initialize(ciutat.getCiutadans());
            }
            return ciutat;
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


    public static <T> List<T> listCollection(Class<T> clazz, String whereClause) {
        return executeInTransactionWithResult(session -> {
            String hql = "FROM " + clazz.getName();
            if (whereClause != null && !whereClause.trim().isEmpty()) {
                hql += " WHERE " + whereClause;
            }
            return session.createQuery(hql, clazz).list();
        });
    }
    public static <T> List<T> listCollection(Class<T> clazz) {
        return listCollection(clazz, "");
    }

    public static <T> String collectionToString(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        for (T obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }


}