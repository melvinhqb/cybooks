package fr.cyu.cybooks.dao;

import java.util.List;
import java.sql.Connection;
import fr.cyu.cybooks.connection.DatabaseConnection;

public abstract class DAO<T> {
    protected Connection conn = null;

    public DAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Creation method
     * @param obj is the object to create
     * @return boolean
     */
    public abstract boolean create(T obj);

    /**
     * Update method
     * @param obj is the object to update
     * @return boolean
     */
    public abstract boolean update(T obj);

    /**
     * Deletion method
     * @param obj is the object to delete
     * @return boolean
     */
    public abstract boolean delete(T obj);

    /**
     * Finding object by id method
     * @param id is the identifier of the finding object
     * @return T
     */
    public abstract T find(int id);

    /**
     * Get all the objects
     */

    public abstract List<T> all();
}
