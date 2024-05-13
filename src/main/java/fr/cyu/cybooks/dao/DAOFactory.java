package fr.cyu.cybooks.dao;

import fr.cyu.cybooks.connection.DatabaseConnection;
import fr.cyu.cybooks.dao.implement.*;

import java.sql.Connection;

public class DAOFactory {
    protected static final Connection conn = DatabaseConnection.getInstance();

    public static UserDAO getUserDAO() {
        return new UserDAO(conn);
    }

    public static LoanDAO getLoanDAO() {
        return new LoanDAO(conn);
    }
}
