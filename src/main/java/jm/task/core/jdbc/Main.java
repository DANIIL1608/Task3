package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        UserService user = new UserServiceImpl(new UserDaoHibernateImpl());

        user.createUsersTable();

        user.saveUser("Арсений", "Петрушков", (byte) 40);
       user.saveUser("Афанасий", "Петров", (byte) 45);
        user.saveUser("Гарельт", "из Ривии", (byte) 120);
        user.saveUser("Эмгыр", "вар Эмрейс", (byte) 29);

        user.removeUserById(1);
        System.out.println(user.getAllUsers().toString());
        user.cleanUsersTable();
        user.dropUsersTable();
    }
}
