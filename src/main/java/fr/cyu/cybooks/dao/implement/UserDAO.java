package fr.cyu.cybooks.dao.implement;

import fr.cyu.cybooks.dao.DAO;
import fr.cyu.cybooks.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) implementation for managing User entities.
 */
public class UserDAO extends DAO<User> {
    /**
     * The name of the table in the database.
     */
    private static final String TABLE_NAME = "users";

    /**
     * Constructs a UserDAO with the specified database connection.
     *
     * @param conn the database connection
     */
    public UserDAO(Connection conn) {
        super(conn);
    }

    /**
     * Executes an update operation (insert or update) on the database.
     *
     * @param query    the SQL query to execute
     * @param obj      the Loan object containing the data to update
     * @param isUpdate whether the operation is an update (true) or an insert (false)
     * @return true if the operation was successful, false otherwise
     */
    public boolean executeUpdate(String query, User obj, boolean isUpdate) {
        try (PreparedStatement statement = conn.prepareStatement(query, isUpdate ? PreparedStatement.NO_GENERATED_KEYS : PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, obj.getFirstName());
            statement.setString(2, obj.getLastName());
            statement.setString(3, obj.getEmail());
            if (isUpdate) {
                statement.setInt(4, obj.getId());
            }

            int rowsAffected = statement.executeUpdate();

            if (!isUpdate && rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        obj.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Maps a ResultSet row to a Loan object.
     *
     * @param resultSet the ResultSet to map
     * @return a Loan object containing the data from the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("firstname");
        String lastName = resultSet.getString("lastname");
        String email = resultSet.getString("email");
        return new User(id, firstName, lastName, email);
    }

    /**
     * Creates a new user record in the database.
     *
     * @param obj the User object to create
     * @return true if the creation was successful, false otherwise
     */
    @Override
    public boolean create(User obj) {
        String query = "INSERT INTO " + TABLE_NAME + " (firstName, lastName, email) VALUES (?, ?, ?)";
        return executeUpdate(query, obj, false);
    }

    /**
     * Updates an existing user record in the database.
     *
     * @param obj the User object to update
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(User obj) {
        String query = "UPDATE " + TABLE_NAME + " SET firstName = ?, lastName = ?, email = ? WHERE id = ?";
        return executeUpdate(query, obj, true);
    }

    /**
     * Deletes a user record from the database.
     *
     * @param user the User object to delete
     * @return true if the deletion was successful, false otherwise
     */
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

    /**
     * Search for users in the database based on the search term.
     * The search term is compared with the firstname, lastname and email fields.
     * @param searchTerm The search term to use to find users.
     * @return A list of users matching the search term.
     */
    public List<User> search(String searchTerm) {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE firstname LIKE ? OR lastname LIKE ? OR email LIKE ?";
            PreparedStatement statement = conn.prepareStatement(query);
            String searchPattern = "%" + searchTerm + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setFirstName(resultSet.getString("firstname"));
                    user.setLastName(resultSet.getString("lastname"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Finds a user record by its ID.
     *
     * @param id the ID of the user to find
     * @return the User object if found, null otherwise
     */
    @Override
    public User find(int id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all user records from the database.
     *
     * @return a list of all User objects
     */
    @Override
    public List<User> all() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
