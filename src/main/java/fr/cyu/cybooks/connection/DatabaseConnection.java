package fr.cyu.cybooks.connection;

import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Manages the database connection.
 * Handles connection establishment, database and tables creation.
 */
public class DatabaseConnection {
    private final static String URL = "jdbc:mysql://localhost:3306/";
    private final static String DB_NAME = "cybooks";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";
    private static Connection conn;

    /**
     * Initializes the database connection.
     * If the database doesn't exist, prompts the user to create it and initializes the tables.
     */
    private DatabaseConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (!databaseExists()) {
                if (!askForDatabaseCreation()) {
                    System.out.println("Création de la base de données annulée.");
                    conn.close();
                    exit(1);
                }
                createDatabase();
                conn = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
                createTables();
            } else {
                conn = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
            }
            System.out.println("Connexion à la base de données " + DB_NAME + " établie.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données. Veuillez vérifier vos identifiants de connexion.");
            System.err.println(e.getMessage());
            exit(1);
        }
    }

    /**
     * Returns the instance of the database connection.
     * Initializes the connection if it doesn't already exist.
     *
     * @return the database connection instance
     */
    public static Connection getInstance() {
        if (conn == null) {
            new DatabaseConnection();
            System.out.println("Instanciation de la connexion SQL.");
        } else {
            System.out.println("Connexion SQL existante.");
        }
        return conn;
    }

    /**
     * Closes the database connection.
     * Logs an error message if the connection closure fails.
     */
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connexion SQL fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    /**
     * Checks if the database exists.
     *
     * @return true if the database exists, false otherwise
     */
    private static boolean databaseExists() {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet resultSet = meta.getCatalogs();
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (databaseName.equals(DB_NAME)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de la base de données : " + e.getMessage());
            exit(1);
        }
        return false;
    }

    /**
     * Asks the user whether to create the database if it doesn't exist.
     *
     * @return true if the user agrees to create the database, false otherwise
     */
    private static boolean askForDatabaseCreation() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("La base de données '" + DB_NAME + "' n'existe pas. Voulez-vous la créer ? (oui/non) ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("oui") || response.equals("yes");
    }

    /**
     * Creates the database.
     * Logs an error message if the database creation fails.
     */
    private static void createDatabase() {
        try {
            Statement statement = conn.createStatement();
            String createDatabaseQuery = "CREATE DATABASE " + DB_NAME;
            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Base de données créée avec succès : " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base de données : " + e.getMessage());
            exit(1);
        }
    }

    /**
     * Creates the necessary tables in the database.
     * Logs an error message if the table creation fails.
     */
    private static void createTables() {
        try {
            Statement statement = conn.createStatement();

            // Create "users" table
            String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "firstName VARCHAR(50) NOT NULL," +
                    "lastName VARCHAR(50) NOT NULL," +
                    "email VARCHAR(50) NOT NULL UNIQUE" +
                    ")";
            statement.executeUpdate(createUserTableQuery);
            System.out.println("Table 'users' créée avec succès.");

            // Create "loans" table
            String createLoansTableQuery = "CREATE TABLE IF NOT EXISTS loans (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "userId INT NOT NULL," +
                    "bookId VARCHAR(50) NOT NULL," +
                    "loanDate DATETIME NOT NULL," +
                    "dueDate DATETIME NOT NULL," +
                    "returnDate DATE," +
                    "FOREIGN KEY (userId) REFERENCES users(id)" +
                    ")";
            statement.executeUpdate(createLoansTableQuery);
            System.out.println("Table 'loans' créée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables : " + e.getMessage());
            exit(1);
        }
    }
}
