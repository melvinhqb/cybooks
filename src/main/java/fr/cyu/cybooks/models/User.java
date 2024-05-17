package fr.cyu.cybooks.models;

import fr.cyu.cybooks.dao.DAOFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
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

    public String getFullName() {
        return firstName + " " + lastName.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Loan> getLoans() {
        return DAOFactory.getLoanDAO().findByUserId(this.id);
    }

    public List<Loan> getCurrentLoans() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    public List<Loan> getOverdueLoans() {
        LocalDateTime currentDate = LocalDateTime.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null && loan.getDueDate().isBefore(currentDate))
                .collect(Collectors.toList());
    }

    public List<Loan> getLoanHistory() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() != null)
                .collect(Collectors.toList());
    }

    public List<Book> getBorrowedBooks() {
        return this.getLoans()
                .stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    public List<Book> getCurrentBorrowedBooks() {
        return this.getCurrentLoans()
                .stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    public List<Book> getCurrentLateBorrowedBooks() {
        LocalDateTime currentDate = LocalDateTime.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getDueDate().isBefore(currentDate))
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    public List<Book> getReturnedBooks() {
        return this.getLoanHistory()
                .stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    public boolean isEligibleToLoan() {
        if (getCurrentLoans().size() >= MAX_LOANS) {
            return false;
        }
        else return getOverdueLoans().isEmpty();
    }

    public static void display(List<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Firstname: " + firstName + "\n" +
                "Lastname: " + lastName + "\n" +
                "Email: " + email;
    }
}
