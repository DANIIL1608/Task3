package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class Util {

    private static Session session;
    private static Transaction transaction;
    private static SessionFactory sessionFactory;

    public static Session openSessionAndTransection() {
        session = getSessionFactory().openSession();
        transaction = session.beginTransaction();
        return session;
    }
    public static void closeALL() {
        if (transaction != null && transaction.isActive()) {
            transaction.commit();
        }

        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure("hibernate.properties");
                configuration.addAnnotatedClass(User.class);
                sessionFactory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Error initializing Hibernate: " + ex);
            }
        }
        return sessionFactory;
    }
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "kharek", "kharek");
        } catch (Exception ex) {
            System.err.println("Connection failed...");
        }
        return connection;
    }
}
