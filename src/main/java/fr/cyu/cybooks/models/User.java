package fr.cyu.cybooks.models;

import fr.cyu.cybooks.dao.DAOFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
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

    public String getFullName() {
        return firstName + " " + lastName.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private List<Loan> getLoans() {
        return DAOFactory.getLoanDAO().findByUserId(this.id);
    }

    public List<Loan> getCurrentLoans() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    private List<Loan> getCurrentLateLoans() {
        LocalDate currentDate = LocalDate.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null && loan.getDueDate().isBefore(currentDate))
                .collect(Collectors.toList());
    }

    private List<Loan> getPastLoans() {
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
        LocalDate currentDate = LocalDate.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getDueDate().isBefore(currentDate))
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    public List<Book> getReturnedBooks() {
        return this.getPastLoans()
                .stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    public boolean isEligibleToLoan() {
        if (getCurrentLoans().size() >= MAX_LOANS) {
            return false;
        }
        else return getCurrentLateLoans().isEmpty();
    }

    @Override
    public String toString() {
        return "ID : " + id + "\n" +
                "PRÉNOM : " + firstName + "\n" +
                "NOM : " + lastName + "\n" +
                "EMAIL : " + email;
    }
}
