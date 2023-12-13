package ru.tinkoff.qa.hibernate;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryCreator {

    private static SessionFactory sessionFactory;

    public static SessionFactory createSessionFactory() {
        try {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Workman.class)
                    .addAnnotatedClass(Animal.class)
                    .addAnnotatedClass(Places.class)
                    .addAnnotatedClass(Zoo.class)
                    .configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return sessionFactory;
    }
}
