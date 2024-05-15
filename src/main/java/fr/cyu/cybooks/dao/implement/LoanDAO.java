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

public class LoanDAO extends DAO<Loan> {
    private static final String TABLE_NAME = "loans";

    public LoanDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(Loan obj) {
        try {
            String query = "INSERT INTO " + TABLE_NAME + " (userId, bookId, loanDate, dueDate, returnDate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, obj.getUser().getId());
            statement.setString(2, obj.getBook().getId());
            statement.setObject(3, obj.getLoanDate());
            statement.setObject(4, obj.getDueDate());
            statement.setObject(5, obj.getReturnDate());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Loan obj) {
        try {
            String query = "UPDATE " + TABLE_NAME + " SET userId = ?, bookId = ?, loanDate = ?, dueDate = ?, returnDate = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, obj.getUser().getId());
            statement.setString(2, obj.getBook().getId());
            statement.setObject(3, obj.getLoanDate());
            statement.setObject(4, obj.getDueDate());
            statement.setObject(5, obj.getReturnDate());
            statement.setInt(6, obj.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Loan obj) {
        try {
            String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, obj.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Loan find(int id) {
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String bookId = resultSet.getString("bookId");
                LocalDate loanDate = resultSet.getObject("loanDate", LocalDate.class);
                LocalDate dueDate = resultSet.getObject("dueDate", LocalDate.class);
                LocalDate returnDate = resultSet.getObject("returnDate", LocalDate.class);
                return new Loan(id, DAOFactory.getUserDAO().find(userId), (new BookAPI()).fetchBookById(bookId), loanDate, dueDate, returnDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Loan> all() {
        return null;
    }

    public List<Loan> findByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE userId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String bookId = resultSet.getString("bookId");
                LocalDate loanDate = resultSet.getObject("loanDate", LocalDate.class);
                LocalDate dueDate = resultSet.getObject("dueDate", LocalDate.class);
                LocalDate returnDate = resultSet.getObject("returnDate", LocalDate.class);
                loans.add(new Loan(id, DAOFactory.getUserDAO().find(userId), (new BookAPI()).fetchBookById(bookId), loanDate, dueDate, returnDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public List<Loan> findByBookId(String bookId) {
        List<Loan> loans = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE bookId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("userId");
                LocalDate loanDate = resultSet.getObject("loanDate", LocalDate.class);
                LocalDate dueDate = resultSet.getObject("dueDate", LocalDate.class);
                LocalDate returnDate = resultSet.getObject("returnDate", LocalDate.class);
                loans.add(new Loan(id, DAOFactory.getUserDAO().find(userId), (new BookAPI()).fetchBookById(bookId), loanDate, dueDate, returnDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }
}
