package fr.cyu.cybooks.models;

public class Book {
    public static final int MAX_LOAN_DAYS = 14;
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

    @Override
    public String toString() {
        return title;
    }
}
