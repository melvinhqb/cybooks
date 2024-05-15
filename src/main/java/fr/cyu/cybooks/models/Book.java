package fr.cyu.cybooks.models;

import fr.cyu.cybooks.dao.DAOFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Book {
    public static final int MAX_LOAN_DAYS = 14;
    public static final int NB_COPIES = 4;
    private String id;
    private String title;
    private String author;
    private String contributors;
    private String Date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() { return this.author; }

    public void setAuthor(String author) { this.author = author; }

    public String getContributors() { return this.contributors; }

    public void setContributors(String contributors) { this.contributors = contributors; }

    public String getDate() { return this.Date; }

    public void setDate(String Date) { this.Date = Date; }

    public List<Loan> getLoans() {
        return DAOFactory.getLoanDAO().findByBookId(this.id);
    }

    public List<Loan> getCurrentLoans() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    public List<Loan> getCurrentLateLoans() {
        LocalDate currentDate = LocalDate.now();
        return this.getCurrentLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() == null && loan.getDueDate().isBefore(currentDate))
                .collect(Collectors.toList());
    }

    public List<Loan> getPastLoans() {
        return this.getLoans()
                .stream()
                .filter(loan -> loan.getReturnDate() != null)
                .collect(Collectors.toList());
    }

    public boolean isAvailableForLoan() {
        return getCurrentLoans().size() < NB_COPIES;
    }

    public static void display(List<Book> list) {
        for (Book book : list) {
            System.out.println(book);
        }
    }

    public static void display(List<Book> list, boolean withIndex) {
        for (int i = 0; i < list.size(); i++) {
            if (withIndex) {
                System.out.print((i + 1) + ". ");
            }
            System.out.println(list.get(i));
        }
    }

    @Override
    public String toString() {
        return title;
    }
}
