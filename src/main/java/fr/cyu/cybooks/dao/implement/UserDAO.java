package fr.cyu.cybooks.dao.implement;

import fr.cyu.cybooks.dao.DAO;
import fr.cyu.cybooks.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO<User> {
    private static final String TABLE_NAME = "users";

    public UserDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(User user) {
        try {
            String query = "INSERT INTO " + TABLE_NAME + " (firstName, lastName, email) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    System.err.println("Aucune clé générée après l'insertion.");
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try {
            String query = "UPDATE " + TABLE_NAME + " SET firstName = ?, lastName = ?, email = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(User user) {
        try {
            String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, user.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public User find(int id) {
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setFirstName(result.getString("firstName"));
                user.setLastName(result.getString("lastName"));
                user.setEmail(result.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> all() {
        List<User> listUsers = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                User user = new User();
                user.setId(result.getInt("id"));
                user.setFirstName(result.getString("firstName"));
                user.setLastName(result.getString("lastName"));
                user.setEmail(result.getString("email"));
                listUsers.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
        return listUsers;
    }
}
