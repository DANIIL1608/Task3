package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static jm.task.core.jdbc.util.Util.*;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;
    private Statement statement;
    private final Connection connection = Util.getConnection();
    @Override
    public void createUsersTable() {
        try {
            statement = connection.createStatement();
            try {
                statement.executeUpdate(("CREATE TABLE `users`.`users` (\n" +
                        "  `ID` INT NOT NULL,\n" +
                        "  `NAME` VARCHAR(45) NOT NULL,\n" +
                        "  `LASTNAME` VARCHAR(45) NOT NULL,\n" +
                        "  `AGE` INT(3) NULL,\n" +
                        "  PRIMARY KEY (`ID`));;"));
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
                statement.executeUpdate("DROP TABLE `users`.`users`;");
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
            session = openSessionAndTransection();
            session.save(new User(name, lastName, age));
            closeALL();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session = openSessionAndTransection();
            session.delete(session.get(User.class, "id"));
            closeALL();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        try {
            session = openSessionAndTransection();
            userList = session.createQuery("from User").getResultList();
            closeALL();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try{
            session = openSessionAndTransection();
            session.createQuery("delete User").executeUpdate();
            closeALL();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
