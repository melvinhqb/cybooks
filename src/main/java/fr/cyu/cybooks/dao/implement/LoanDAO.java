package fr.cyu.cybooks.dao.implement;

import fr.cyu.cybooks.dao.DAO;
import fr.cyu.cybooks.dao.DAOFactory;
import fr.cyu.cybooks.dao.api.BookAPI;
import fr.cyu.cybooks.models.Loan;
import fr.cyu.cybooks.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) implementation for managing Loan entities.
 */
public class LoanDAO extends DAO<Loan> {
    /**
     * The name of the table in the database.
     */
    private static final String TABLE_NAME = "loans";

    /**
     * Constructs a LoanDAO with the specified database connection.
     *
     * @param conn the database connection
     */
    public LoanDAO(Connection conn) {
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
    private boolean executeUpdate(String query, Loan obj, boolean isUpdate) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, obj.getUser().getId());
            statement.setString(2, obj.getBook().getId());
            statement.setObject(3, obj.getLoanDate());
            statement.setObject(4, obj.getDueDate());
            statement.setObject(5, obj.getReturnDate());
            if (isUpdate) {
                statement.setInt(6, obj.getId());
            }
            int rowsAffected = statement.executeUpdate();
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
    private Loan mapResultSetToLoan(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("userId");
        String bookId = resultSet.getString("bookId");
        LocalDate loanDate = resultSet.getObject("loanDate", LocalDate.class);
        LocalDate dueDate = resultSet.getObject("dueDate", LocalDate.class);
        LocalDate returnDate = resultSet.getObject("returnDate", LocalDate.class);
        User user = DAOFactory.getUserDAO().find(userId);
        BookAPI bookAPI = new BookAPI();
        return new Loan(id, user, bookAPI.fetchBookById(bookId), loanDate, dueDate, returnDate);
    }

    /**
     * Creates a new loan record in the database.
     *
     * @param obj the Loan object to create
     * @return true if the creation was successful, false otherwise
     */
    @Override
    public boolean create(Loan obj) {
        String query = "INSERT INTO " + TABLE_NAME + " (userId, bookId, loanDate, dueDate, returnDate) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(query, obj, false);
    }

    /**
     * Updates an existing loan record in the database.
     *
     * @param obj the Loan object to update
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(Loan obj) {
        String query = "UPDATE " + TABLE_NAME + " SET userId = ?, bookId = ?, loanDate = ?, dueDate = ?, returnDate = ? WHERE id = ?";
        return executeUpdate(query, obj, true);
    }

    /**
     * Deletes a loan record from the database.
     *
     * @param obj the Loan object to delete
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete(Loan obj) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, obj.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a loan record by its ID.
     *
     * @param id the ID of the loan to find
     * @return the Loan object if found, null otherwise
     */
    @Override
    public Loan find(int id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToLoan(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all loan records from the database.
     *
     * @return a list of all Loan objects
     */
    @Override
    public List<Loan> all() {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                loans.add(mapResultSetToLoan(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    /**
     * Finds all loan records by the user ID.
     *
     * @param userId the ID of the user
     * @return a list of Loan objects for the specified user
     */
    public List<Loan> findByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE userId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    loans.add(mapResultSetToLoan(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    /**
     * Finds all loan records by the book ID.
     *
     * @param bookId the ID of the book
     * @return a list of Loan objects for the specified book
     */
    public List<Loan> findByBookId(String bookId) {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE bookId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    loans.add(mapResultSetToLoan(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }
}
