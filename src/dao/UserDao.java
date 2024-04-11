package dao;

import entity.User;
import core.DatabaseConnection;
import sun.security.krb5.internal.EncAPRepPart;

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

    public boolean save(User user) {
        String query = "INSERT INTO public.users " +
                "(" +
                "username," +
                "password," +
                "role" +
                ")" +
                " VALUES (?, ?, ?)";

        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setString(1, user.getUsername());
            pr.setString(2, user.getPassword());
            pr.setString(3, user.getRole());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean update(User user) {
        String query = "UPDATE public.users SET " +
                "username = ? , " +
                "password = ? , " +
                "role = ? " +
                "WHERE user_id = ?";

        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setString(1, user.getUsername());
            pr.setString(2, user.getPassword());
            pr.setString(3, user.getRole());
            pr.setInt(4, user.getUserId());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean delete(int id){
        String query = "DELETE FROM public.users WHERE user_id = ?";

        try{
            PreparedStatement pr = databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public User getById(int id) {
        User obj = null;
        String query = "SELECT * FROM public.users WHERE user_id = ?";
        try {
            PreparedStatement pr = this.databaseConnection.getConnection().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = this.match(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM public.users";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
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

    public List<User> selectByQuery(String query){
        List<User> users = new ArrayList<>();
        try {
            ResultSet rs = this.databaseConnection.getConnection().createStatement().executeQuery(query);
            while (rs.next()) {
                users.add(this.match(rs));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return users;
    }

    public User match(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }



}