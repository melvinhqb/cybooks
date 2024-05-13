package fr.cyu.cybooks.connection;

import java.sql.*;
import java.util.Scanner;

public class DatabaseConnection {
    private final static String URL = "jdbc:mysql://localhost:3306/";
    private final static String DB_NAME = "cybooks";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";
    private static Connection conn;

    private DatabaseConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            //System.out.println("Connexion MySQL établie.");
            if (!databaseExists()) {
                if (!askForDatabaseCreation()) {
                    System.out.println("Création de la base de données annulée.");
                    conn.close();
                    System.exit(0);
                }
                createDatabase();
                conn = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
                createTables();
            } else {
                conn = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
            }
            System.out.println("Connexion à la base de données " + DB_NAME + " établie.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if (conn == null) {
            new DatabaseConnection();
            System.out.println("Instanciation de la connexion SQL.");
        } else {
            System.out.println("Connexion SQL existante.");
        }
        return conn;
    }

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
        }
        return false;
    }

    private static boolean askForDatabaseCreation() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("La base de données '" + DB_NAME + "' n'existe pas. Voulez-vous la créer ? (oui/non) ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("oui") || response.equals("yes");
    }

    private static void createDatabase() {
        try {
            Statement statement = conn.createStatement();
            String createDatabaseQuery = "CREATE DATABASE " + DB_NAME;
            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Base de données créée avec succès : " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base de données : " + e.getMessage());
        }
    }

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
                    "loanDate DATE NOT NULL," +
                    "dueDate DATE NOT NULL," +
                    "returnDate DATE," +
                    "FOREIGN KEY (userId) REFERENCES users(id)" +
                    ")";
            statement.executeUpdate(createLoansTableQuery);
            System.out.println("Table 'loans' créée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables : " + e.getMessage());
        }
    }

    private static void createLoansTable() {
        try {
            Statement statement = conn.createStatement();
            String createLoansTableQuery = "CREATE TABLE IF NOT EXISTS loans (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "userId INT NOT NULL," +
                    "bookId VARCHAR(50) NOT NULL," +
                    "loanDate DATE NOT NULL," +
                    "dueDate DATE NOT NULL," +
                    "returnDate DATE," +
                    "FOREIGN KEY (userId) REFERENCES users(id)," +
                    ")";
            statement.executeUpdate(createLoansTableQuery);
            System.out.println("Table 'loans' créée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table 'loans' : " + e.getMessage());
        }
    }
}
