package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?,?,?)";

    //add users

    public void create(User user) {
        try{
            Connection connection = DbUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                System.out.println("inserted ID: " + id);
            }
        }   catch (SQLException e) {
            System.out.println("Błąd dodawania użytkownika, spróbuj ponownie");
            e.printStackTrace();
        }
    }
    private String hashPassword(String password) {
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }

    //edit data
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    public User read(int id) {
        try {
            Connection connection = DbUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return readUser(resultSet);
            }
            }   catch (SQLException e) {
            System.out.println("Błąd pobierania numeru id, spróbuj ponownie");
            e.printStackTrace();
        }
        return null;
    }

    //delete id
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    public void delete (int id) {
        try{
            Connection connection = DbUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }   catch (SQLException e) {
            System.out.println("Błąd usuwania numeru id, spróbuj ponownie");
            e.printStackTrace();
        }
    }
    //downloading all users
    private static final String SELECT_USERS_QUERY = "SELECT * FROM users";
    public List<User> findAll() {
        List<User>users = new ArrayList<>();
        try {
            Connection connection = DbUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = readUser(resultSet);
                users.add(user);
            }
        }   catch (SQLException e) {
            System.out.println("Błąd pobierania wszystkich użytkowników, spróbuj ponownie");
            e.printStackTrace();
        }
        return users;
    }
    private User readUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String email = resultSet.getString(3);
        String password = resultSet.getString(4);
        return new User(userId, username, email, password);
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }
}
