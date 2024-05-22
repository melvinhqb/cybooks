package fr.cyu.cybooks.models;

import fr.cyu.cybooks.dao.DAOFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a book entity with its attributes and methods.
 */
public class Book {
    public static final int MAX_LOAN_DAYS = 14;
    public static final int NB_COPIES = 4;
    private String id;
    private String title;
    private String author;
    private String contributors;
    private String Date;

    /**
     * Get the unique identifier of the book.
     *
     * @return the unique identifier of the book
     */
    public String getId() {
        return id;
    }

    /**
     * Set the unique identifier of the book.
     *
     * @param id the unique identifier of the book
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the title of the book
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the book
     *
     * @param title is the title of the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the author of the book
     *
     * @return the author of the book
     */
    public String getAuthor() { return this.author; }

    /**
     * Set the author of the book
     * @param author is the author of the book
     */
    public void setAuthor(String author) { this.author = author; }

    /**
     * Get the contributors of the book
     *
     * @return the contributors of the book
     */
    public String getContributors() { return this.contributors; }

    /**
     * Set the contributors of the book
     *
     * @param contributors is the contributors of the book
     */
    public void setContributors(String contributors) { this.contributors = contributors; }

    /**
     * Get the published date of the book
     *
     * @return the published date of the book
     */
    public String getDate() { return this.Date; }

    /**
     * Set the published date of the book
     *
     * @param Date is the published date of the book
     */
    public void setDate(String Date) { this.Date = Date; }

    /**
     * Retrieves the list of loans associated with this book.
     *
     * @return the list of loans associated with this book
     */
    public List<Loan> getLoans() {
        return DAOFactory.getLoanDAO().findByBookId(this.id);
    }

    /**
     * Retrieves the list of current loans (not returned) associated with this book.
     *
     * @return the list of current loans associated with this book
     */
    public List<Loan> getCurrentLoans() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the list of overdue loans associated with this book.
     *
     * @return the list of overdue loans associated with this book
     */
    public List<Loan> getOverdueLoans() {
        LocalDateTime currentDate = LocalDateTime.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null && loan.getDueDate().isBefore(currentDate))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the loan history (returned loans) associated with this book.
     *
     * @return the loan history associated with this book
     */
    public List<Loan> getLoanHistory() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() != null)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the book is available for loan based on the number of current loans.
     *
     * @return true if the book is available for loan, otherwise false
     */
    public boolean isAvailableForLoan() {
        return getCurrentLoans().size() < NB_COPIES;
    }

    /**
     * Displays the details of a book.
     *
     * @param book the book to display
     * @param withIndex whether to display an index number
     * @param index the index number
     */
    private static void displayBookDetails(Book book, boolean withIndex, int index) {
        String title = (book.getTitle() != null) ? book.getTitle() : "Unknown Title";
        String author = (book.getAuthor() != null) ? book.getAuthor() : "Unknown author";
        String date = (book.getDate() != null) ? book.getDate() : "Unknown date";
        String contributors = (book.getContributors() != null) ? book.getContributors() : "No contributors";

        if (withIndex) {
            System.out.print((index) + ". ");
        }
        System.out.printf("%-30s;\t%-30s;\t%-4s;\t%-30s;\n",
                title,
                author,
                date,
                contributors);
    }

    /**
     * Displays the list of books with their details.
     *
     * @param list the list of books to display
     */
    public static void display(List<Book> list) {
        for (Book book : list) {
            displayBookDetails(book, false, 0);
        }
    }

    /**
     * Displays the list of books with their details along with index numbers.
     *
     * @param list       the list of books to display
     * @param withIndex  whether to display index numbers or not
     */
    public static void display(List<Book> list, boolean withIndex) {
        int count = 1;
        for (Book book : list) {
            displayBookDetails(book, withIndex, count++);
        }
    }

    /**
     * Overrides the toString method to return the title of the book.
     *
     * @return the title of the book
     */
    @Override
    public String toString() {
        return title;
    }
}
