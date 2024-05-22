package fr.cyu.cybooks.models;

import fr.cyu.cybooks.dao.DAOFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a user entity with its attributes and methods.
 */
public class User {
    public static final int MAX_LOANS = 5;
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Default constructor for User class.
     * Initializes firstName, lastName and email to null.
     */
    public User() {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
    }

    /**
     * Parameterized constructor for User class.
     *
     * @param id        the user ID
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email address
     */
    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Get the user ID.
     *
     * @return the user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id the user ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user's first name.
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's full name.
     *
     * @return the user's full name
     */
    public String getFullName() {
        return firstName + " " + lastName.toUpperCase();
    }

    /**
     * Gets the user's email address.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the list of loans associated with the user.
     *
     * @return the list of loans associated with the user
     */
    public List<Loan> getLoans() {
        return DAOFactory.getLoanDAO().findByUserId(this.id);
    }

    /**
     * Gets the list of current loans associated with the user.
     *
     * @return the list of current loans associated with the user
     */
    public List<Loan> getCurrentLoans() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    /**
     * Gets the list of overdue loans associated with the user.
     *
     * @return the list of overdue loans associated with the user
     */
    public List<Loan> getOverdueLoans() {
        LocalDateTime currentDate = LocalDateTime.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null && loan.getDueDate().isBefore(currentDate))
                .collect(Collectors.toList());
    }

    /**
     * Gets the list of loan history associated with the user.
     *
     * @return the list of loan history associated with the user
     */
    public List<Loan> getLoanHistory() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() != null)
                .collect(Collectors.toList());
    }

    /**
     * Gets the list of borrowed books associated with the user.
     *
     * @return the list of borrowed books associated with the user
     */
    public List<Book> getBorrowedBooks() {
        return this.getLoans()
                .stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    /**
     * Gets the list of currently borrowed books associated with the user.
     *
     * @return the list of currently borrowed books associated with the user
     */
    public List<Book> getCurrentBorrowedBooks() {
        return this.getCurrentLoans()
                .stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    /**
     * Gets the list of currently late borrowed books associated with the user.
     *
     * @return the list of currently late borrowed books associated with the user
     */
    public List<Book> getCurrentLateBorrowedBooks() {
        LocalDateTime currentDate = LocalDateTime.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getDueDate().isBefore(currentDate))
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    /**
     * Gets the list of returned books associated with the user.
     *
     * @return the list of returned books associated with the user
     */
    public List<Book> getReturnedBooks() {
        return this.getLoanHistory()
                .stream()
                .map(Loan::getBook)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the user is eligible to borrow books.
     *
     * @return true if the user is eligible to borrow books, otherwise false
     */
    public boolean isEligibleToLoan() {
        if (getCurrentLoans().size() >= MAX_LOANS) {
            return false;
        }
        else return getOverdueLoans().isEmpty();
    }

    /**
     * Displays the list of users with their details.
     *
     * @param users the list of users to display
     */
    public static void display(List<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * Overrides the toString method to return the attributes of a user
     *
     * @return the title of the book
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Firstname: " + firstName + "\n" +
                "Lastname: " + lastName + "\n" +
                "Email: " + email;
    }
}
