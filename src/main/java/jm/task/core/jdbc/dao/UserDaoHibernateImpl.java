package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static jm.task.core.jdbc.util.Util.*;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;
    private Statement statement;
    private final Connection connection = Util.getConnection();
    private Transaction transaction;
    @Override
    public void createUsersTable() {
        try {
            statement = connection.createStatement();
            try {
                statement.executeUpdate(("CREATE TABLE user (\n" +
                        "  `ID` INT NOT NULL,\n" +
                        "  `NAME` VARCHAR(45) NOT NULL,\n" +
                        "  `LASTNAME` VARCHAR(45) NOT NULL,\n" +
                        "  `AGE` INT(3) NOT NULL,\n" +
                        "  PRIMARY KEY (`ID`));"));
            } catch (Throwable e1) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Throwable e2) {
                        e1.addSuppressed(e2);
                    }
                }
                throw e1;
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println("Таблицы закончились, заходи завтра.");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            statement = connection.createStatement();
            try {
                statement.executeUpdate("DROP TABLE IF EXISTS user;");
            } catch (Throwable e1) {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Throwable e2) {
                        e1.addSuppressed(e2);
                    }
                }
                throw e1;
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println("Таблица не существует");
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(new User(name, lastName, age));
            transaction.commit();
            session.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
            session.close();
        } catch (Throwable t) {
            System.out.println("Ошибка при удалении");
            t.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            List<User> userList = session.createQuery("from User").getResultList();
            transaction.commit();
            session.close();
            return userList;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @Override
    public void cleanUsersTable() {
        try{
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
            session.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
