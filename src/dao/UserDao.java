package dao;

import entity.User;
import core.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final DatabaseConnection databaseConnection;

    public UserDao() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM public.users";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                userList.add(match(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public User findByLogin(String username, String password) {
        String query = "SELECT * FROM public.users WHERE username = ? AND password = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return match(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User match(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}