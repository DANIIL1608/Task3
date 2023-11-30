package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    private Transaction transaction;
    @Override
    public void createUsersTable() throws SQLException{
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(("CREATE TABLE user (\n" +
                    "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `NAME` VARCHAR(45) NOT NULL,\n" +
                    "  `LASTNAME` VARCHAR(45) NOT NULL,\n" +
                    "  `AGE` INT(3) NOT NULL,\n" +
                    "  PRIMARY KEY (`ID`));"));
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Таблицы закончились, заходи завтра.");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("DROP TABLE IF EXISTS user;");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Таблица не существует");
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(new User(name, lastName, age));
            transaction.commit();
        } catch (Throwable t) {
            transaction.rollback();
            t.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
        } catch (Throwable t) {
            System.out.println("Ошибка при удалении");
            transaction.rollback();
            t.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            userList = session.createQuery("from User", User.class).list();
            transaction.commit();
        } catch (Throwable throwable) {
            transaction.rollback();
            throwable.printStackTrace();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
        } catch (Throwable t) {
            transaction.rollback();
            t.printStackTrace();
        }
    }
}
