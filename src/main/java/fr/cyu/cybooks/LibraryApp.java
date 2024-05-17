package fr.cyu.cybooks;

import fr.cyu.cybooks.dao.DAOFactory;
import fr.cyu.cybooks.dao.api.BookAPI;
import fr.cyu.cybooks.dao.implement.LoanDAO;
import fr.cyu.cybooks.dao.implement.UserDAO;
import fr.cyu.cybooks.models.Book;
import fr.cyu.cybooks.models.Loan;
import fr.cyu.cybooks.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = DAOFactory.getUserDAO();
    private static final LoanDAO loanDAO = DAOFactory.getLoanDAO();
    private static final BookAPI bookApi = new BookAPI();

    public static void main(String[] args) {
        displayMainMenu();
    }

    public static void displayMainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. User Management");
            System.out.println("2. Book Search (TO TO)");
            System.out.println("3. Loan Management");
            System.out.println("4. Statistics");
            System.out.println("5. Quit");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> displayUserManagementMenu();
                case 2 -> displayBookSearchMenu();
                case 3 -> displayLoanManagementMenu();
                case 4 -> displayStatisticsMenu();
                case 5 -> {
                    System.out.println("Exiting the application...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayUserManagementMenu() {
        while (true) {
            System.out.println("\n=== User Management ===");
            System.out.println("1. Register New User");
            System.out.println("2. Modify User Details");
            System.out.println("3. Search User");
            System.out.println("4. Display User Details");
            System.out.println("5. Return to Main Menu");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> registerNewUser();
                case 2 -> modifyUserDetails();
                case 3 -> searchUser();
                case 4 -> displayUserMenu();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerNewUser() {
        System.out.println("\n=== Register New User ===");
        System.out.print("Firstname : ");
        String firstName = scanner.nextLine();
        System.out.print("Lastname : ");
        String lastName = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();

        User newUser = new User(0, firstName, lastName, email);

        boolean success = userDAO.create(newUser);

        if (success) {
            System.out.println("User registered successfully.");
        } else {
            System.out.println("Failed to register user.");
        }
    }

    private static void modifyUserDetails() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userDAO.find(userId);

        if (user != null) {
            System.out.println("\n=== Modify User Details ===");
            System.out.printf("Firstname (%s) : ", user.getFirstName());
            user.setFirstName(scanner.nextLine());
            System.out.printf("Lastname (%s) : ", user.getLastName());
            user.setLastName(scanner.nextLine());
            System.out.printf("Email (%s) : ", user.getEmail());
            user.setEmail(scanner.nextLine());

            boolean success = userDAO.update(user);

            if (success) {
                System.out.println("User edited successfully.");
            } else {
                System.out.println("Failed to edit user.");
            }
        }
    }

    private static void searchUser() {
        System.out.println("\n=== Search User ===");
        System.out.print("Enter search term : ");
        String searchTerm = scanner.nextLine();
        List<User> results = userDAO.search(searchTerm);
        if (!results.isEmpty()) {
            System.out.println("Number of results : " + results.size());
            User.display(results);
        } else {
            System.out.println("No users found with search term : '" + searchTerm + "'");
        }
    }

    private static void displayUserMenu() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userDAO.find(userId);

        if (user != null) {
            while (true) {
                System.out.println("\n=== User Menu ===");
                System.out.println(user);
                System.out.println("---");
                System.out.println("1. Display Current Loans (TO DO)");
                System.out.println("2. Display Overdue Loans (TO DO)");
                System.out.println("3. Display History Loans");
                System.out.println("4. Return to Main Menu");

                int choice = getUserChoice();

                switch (choice) {
                    case 1 -> displayUserCurrentLoans(user);
                    case 2 -> displayUserOverdueLoans(user);
                    case 3 -> displayUserLoanHistory(user);
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        } else {
            System.out.println("User with ID: " + userId + " not found.");
        }
    }

    private static void displayUserCurrentLoans(User user) {
    }

    private static void displayUserOverdueLoans(User user) {
    }

    private static void displayUserLoanHistory(User user) {
        List<Loan> loanHistory = user.getLoanHistory();

        if (!loanHistory.isEmpty()) {
            System.out.println("Loan history for user: " + user.getFirstName() + " " + user.getLastName());
            for (Loan loan : loanHistory) {
                System.out.println("Loan ID: " + loan.getId());
                System.out.println("Book: " + loan.getBook().getTitle());
                System.out.println("Loan Date: " + loan.getLoanDate());
                System.out.println("Due Date: " + loan.getDueDate());
                System.out.println();
            }
        } else {
            System.out.println("No loan history found for user with ID: " + user.getId());
        }
    }

    private static void displayBookSearchMenu() {
        while (true) {
            System.out.println("\n=== Book Search ===");
            System.out.println("1. Search by Title (TO DO)");
            System.out.println("2. Search by Author (TO DO)");
            System.out.println("3. Search by Category (TO DO)");
            System.out.println("4. Search by Publication Year (TO DO)");
            System.out.println("5. Return to Main Menu");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> searchBooksByTitle();
                case 2 -> searchBooksByAuthor();
                case 3 -> searchBooksByCategory();
                case 4 -> searchBooksByPublicationYear();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void searchBooksByTitle() {
    }

    private static void searchBooksByAuthor() {
    }

    private static void searchBooksByCategory() {
    }

    private static void searchBooksByPublicationYear() {
    }

    public static void displayLoanManagementMenu() {
        while (true) {
            System.out.println("\n=== Loan Management ===");
            System.out.println("1. Register New Loan");
            System.out.println("2. Register Book Return");
            System.out.println("3. Display Current Loans");
            System.out.println("4. Display Overdue Loans");
            System.out.println("5. Return to Main Menu");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> registerNewLoan();
                case 2 -> registerBookReturn();
                case 3 -> displayCurrentLoans();
                case 4 -> displayOverdueLoans();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerNewLoan() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userDAO.find(userId);

        System.out.println("Verify the eligibility to loan...");

        if (user == null) {
            System.out.println("User with ID: " + userId + " not found.");
        } else if (!user.isEligibleToLoan()) {
            System.out.println("User with ID: " + userId + " is not eligible to loan.");
        } else {
            System.out.print("Enter book title : ");
            String title = scanner.nextLine();

            bookApi.addFilter("title", title);
            List<Book> results = bookApi.searchBooksByMap();

            if (!results.isEmpty()) {
                Book.display(results, true);
                int choice = getUserChoice();

                Book book = results.get(choice-1);
                Loan loan = new Loan(book, user);

                if (loanDAO.create(loan)) {
                    System.out.println("'" + book.getTitle() + "' added to the loan list of " + user.getFullName() + ".");
                } else {
                    System.err.println("Error during the loan process.");
                }

            } else {
                System.out.println("No books found with title '" + title + "'.");
            }
        }
    }

    private static void registerBookReturn() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userDAO.find(userId);

        if (user != null) {
            List<Loan> loansList = user.getCurrentLoans();
            if (!loansList.isEmpty()) {
                List<Book> bookList = new ArrayList<>();
                System.out.println("List of books borrowed by " + user.getFullName() + ": ");
                for (Loan loan : loansList) {
                    bookList.add(loan.getBook());
                }
                Book.display(bookList, true);
                int choice = getUserChoice();

                Loan loan = loansList.get(choice-1);
                loan.setReturnDate(LocalDate.now());

                if (loanDAO.update(loan)) {
                    System.out.println("Book returned successfully.");
                } else {
                    System.err.println("Error during the return process.");
                }
            } else {
                System.out.println("No book to return.");
            }
        } else {
            System.out.println("User with ID: " + userId + " not found.");
        }
    }

    private static void displayCurrentLoans() {
        System.out.println("\n=== Current Loans ===");

        List<Loan> currentLoans = loanDAO.getCurrentLoans();

        if (currentLoans.isEmpty()) {
            System.out.println("No current loans.");
            return;
        }

        for (Loan loan : currentLoans) {
            System.out.println(loan);
        }
    }

    private static void displayOverdueLoans() {
        System.out.println("\n=== Overdue Loans ===");

        List<Loan> overdueLoans = loanDAO.getOverdueLoans();

        if (overdueLoans.isEmpty()) {
            System.out.println("No overdue loans.");
            return;
        }

        for (Loan loan : overdueLoans) {
            System.out.println(loan);
        }
    }

    public static void displayStatisticsMenu() {
        while (true) {
            System.out.println("\n=== Statistics ===");
            System.out.println("1. Most Loaned Books (Last 30 Days)  (TO DO)");
            System.out.println("2. Return to Main Menu");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> displayMostLoanedBooks();
                case 2 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayMostLoanedBooks() {
        System.out.println("\n=== Most Loaned Books ===");

        Map<String, Integer> mostLoanedBooks = loanDAO.getMostLoanedBooks();

        if (mostLoanedBooks.isEmpty()) {
            System.out.println("No books have been loaned yet.");
            return;
        }

        for (Map.Entry<String, Integer> entry : mostLoanedBooks.entrySet()) {
            String bookId = entry.getKey();
            int loanCount = entry.getValue();
            System.out.println("Book ID: " + bookId + " | Loan Count: " + loanCount);
        }
    }

    private static int getUserChoice() {
        System.out.print("\nMake your choice : ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
}
