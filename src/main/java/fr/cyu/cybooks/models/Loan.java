package fr.cyu.cybooks.models;

import java.time.LocalDateTime;

/**
 * Represents a loan of a book to a user.
 */
public class Loan {
    private int id;
    private Book book;
    private User user;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    /**
     * Constructs a Loan object with the specified attributes.
     *
     * @param id         the unique identifier of the loan
     * @param user       the user who borrowed the book
     * @param book       the book being borrowed
     * @param loanDate   the date when the book was borrowed
     * @param dueDate    the due date for returning the book
     * @param returnDate the date when the book was returned (null if not returned yet)
     */
    public Loan(int id, User user, Book book, LocalDateTime loanDate, LocalDateTime dueDate, LocalDateTime returnDate) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    /**
     * Constructs a Loan object with the specified book and user.
     * The loan date is set to the current date and the due date is calculated based on the maximum loan days of the book.
     *
     * @param book the book being borrowed
     * @param user the user who borrowed the book
     */
    public Loan(Book book, User user) {
        this.book = book;
        this.user = user;
        this.loanDate = LocalDateTime.now();
        this.dueDate = this.loanDate.plusDays(Book.MAX_LOAN_DAYS);
        this.returnDate = null;
    }

    /**
     * Constructs a Loan object with the specified book, user, and maximum loan seconds.
     * The loan date is set to the current date and the due date is calculated based on the specified maximum loan seconds.
     *
     * @param book           the book being borrowed
     * @param user           the user who borrowed the book
     * @param maxLoanSeconds the maximum duration of the loan in seconds
     */
    public Loan(Book book, User user, int maxLoanSeconds) {
        this.book = book;
        this.user = user;
        this.loanDate = LocalDateTime.now();
        this.dueDate = this.loanDate.plusSeconds(maxLoanSeconds);
        this.returnDate = null;
    }

    /**
     * Get the unique identifier of the loan
     *
     * @return the unique identifier of the loan
     */
    public int getId() {
        return id;
    }

    /**
     * Set the unique identifier of the loan
     *
     * @param id is the unique identifier of the loan
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the {@link Book} object associated with the loan
     *
     * @return the {@link Book} object associated with the loan
     */
    public Book getBook() {
        return book;
    }

    /**
     * Set the {@link Book} object associated with the loan
     *
     * @param book is the {@link Book} object associated with the loan
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Get the {@link User} object associated with the loan
     *
     * @return the {@link User} object associated with the loan
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the {@link User} object associated with the loan
     *
     * @param user is the {@link User} object associated with the loan
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the loan datetime of the loan
     *
     * @return the loan datetime of the loan
     */
    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    /**
     * Set the loan datetime of the loan
     *
     * @param loanDate is the loan datetime of the loan
     */
    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * Get the due datetime of the loan
     *
     * @return the due datetime of the loan
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Set the due datetime of the loan
     *
     * @param dueDate is the due datetime of the loan
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Get the return datetime of the loan
     *
     * @return the return datetime of the loan
     */
    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    /**
     * Set the return datetime of the loan
     *
     * @param returnDate is the return datetime of the loan
     */
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Overrides the toString method to return the attributes of a loan record
     *
     * @return the title of the book
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Book: " + book.getTitle() + " (ID: " + book.getId() + ")\n" +
                "User: " + user.getFullName() + " (ID: " + user.getId() + ")\n" +
                "Loan Date: " + loanDate + "\n" +
                "Due Date: " + dueDate;
    }
}
