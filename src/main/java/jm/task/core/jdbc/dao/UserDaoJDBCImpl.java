package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.getConnection();

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
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
        } catch (SQLException e) {
            System.err.println("Таблицы закончились, заходи завтра.");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
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
        } catch (SQLException e) {
            System.err.println("Таблица не существует");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(`NAME`, `LASTNAME`, `AGE`) values(?, ?, ?)")) {
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
            } catch (Throwable e1) {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (Throwable e2) {
                        e1.addSuppressed(e2);
                    }
                }
                throw e1;
            }
        } catch (SQLException e) {
            System.err.println("Не сегодня(сохранение)");
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from users.users where ID = ?;")) {
            try {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (Throwable e1) {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (Throwable e2) {
                        e1.addSuppressed(e2);
                    }
                }
                throw e1;
            }
        } catch (SQLException e) {
            System.err.println("Персонаж под таким id не существует");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> sheet = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM users.users");
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("ID"));
                    user.setName(resultSet.getString("NAME"));
                    user.setLastName(resultSet.getString("LASTNAME"));
                    user.setAge(resultSet.getByte("AGE"));
                    sheet.add(user);
                }
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
        } catch (SQLException e) {
            System.err.println("Не судьба(все пользователи)");
            e.printStackTrace();
        }
        return sheet;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            try {
                statement.execute("TRUNCATE TABLE users.users");
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
        } catch (SQLException e) {
            System.err.println("Не судьба(чистка)");
            e.printStackTrace();
        }
    }
}
