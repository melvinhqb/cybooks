package fr.cyu.cybooks.models;

import java.time.LocalDateTime;

public class Loan {
    private int id;
    private Book book;
    private User user;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    public Loan(int id, User user, Book book, LocalDateTime loanDate, LocalDateTime dueDate, LocalDateTime returnDate) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Loan(Book book, User user) {
        this.book = book;
        this.user = user;
        this.loanDate = LocalDateTime.now();
        this.dueDate = this.loanDate.plusDays(Book.MAX_LOAN_DAYS);
        this.returnDate = null;
    }

    public Loan(Book book, User user, int maxLoanSeconds) {
        this.book = book;
        this.user = user;
        this.loanDate = LocalDateTime.now();
        this.dueDate = this.loanDate.plusSeconds(maxLoanSeconds);
        this.returnDate = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Book: " + book.getTitle() + " (ID: " + book.getId() + ")\n" +
                "User: " + user.getFullName() + " (ID: " + user.getId() + ")\n" +
                "Loan Date: " + loanDate + "\n" +
                "Due Date: " + dueDate;
    }
}
