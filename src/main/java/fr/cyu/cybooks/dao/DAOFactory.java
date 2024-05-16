package fr.cyu.cybooks.dao;

import fr.cyu.cybooks.connection.DatabaseConnection;
import fr.cyu.cybooks.dao.implement.*;

import java.sql.Connection;

/**
 * Factory class to provide instances of DAO (Data Access Object) classes.
 * This factory uses a single database connection for all DAO instances.
 */
public class DAOFactory {
    /**
     * The shared database connection instance.
     */
    protected static final Connection conn = DatabaseConnection.getInstance();

    /**
     * Provides an instance of {@link UserDAO} using the shared database connection.
     *
     * @return an instance of {@link UserDAO}
     */
    public static UserDAO getUserDAO() {
        return new UserDAO(conn);
    }

    /**
     * Provides an instance of {@link LoanDAO} using the shared database connection.
     *
     * @return an instance of {@link LoanDAO}
     */
    public static LoanDAO getLoanDAO() {
        return new LoanDAO(conn);
    }
}
