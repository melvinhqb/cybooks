package fr.cyu.cybooks.models;

import java.util.List;
import java.util.Locale;

public class User {
    public static final int MAX_LOANS = 5;
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public User() {
        this.firstName = null;
        this.lastName = null;
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Loan> currentLoans() {
        // On appelle une méthode dans LoanDAO qui execute la requête SQL
        return null;
    }

    @Override
    public String toString() {
        return "ID : " + id + "\n" +
                "PRÉNOM : " + firstName + "\n" +
                "NOM : " + lastName + "\n" +
                "EMAIL : " + email + "\n";
    }
}
