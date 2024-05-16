package fr.cyu.cybooks.dao;

import java.util.List;
import java.sql.Connection;

/**
 * Abstract base class for Data Access Objects (DAOs).
 * This class provides a generic interface for CRUD operations.
 *
 * @param <T> the type of the object that this DAO will manage
 */
public abstract class DAO<T> {
    /**
     * The database connection used by this DAO.
     */
    protected Connection conn;

    /**
     * Constructs a new DAO with the specified database connection.
     *
     * @param conn the database connection to be used by this DAO
     */
    public DAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Creates a new object record in the database
     * @param obj is the object to create
     * @return true if the creation was successful, false otherwise
     */
    public abstract boolean create(T obj);

    /**
     * Updates an existing object record in the database.
     * @param obj is the object to update
     * @return true if the update was successful, false otherwise
     */
    public abstract boolean update(T obj);

    /**
     * Deletes an object record from the database
     * @param obj is the object to delete
     * @return true if the deletion was successful, false otherwise
     */
    public abstract boolean delete(T obj);

    /**
     * Finds an object record by its ID
     * @param id the ID of the object to find
     * @return T if found, null otherwise
     */
    public abstract T find(int id);

    /**
     * Retrieves all T object records from the database
     * @return a list of all T objects
     */
    public abstract List<T> all();
}
