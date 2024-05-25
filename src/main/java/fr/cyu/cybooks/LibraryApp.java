package fr.cyu.cybooks;

import fr.cyu.cybooks.dao.DAOFactory;
import fr.cyu.cybooks.dao.api.BookAPI;
import fr.cyu.cybooks.dao.implement.LoanDAO;
import fr.cyu.cybooks.dao.implement.UserDAO;
import fr.cyu.cybooks.models.Book;
import fr.cyu.cybooks.models.Loan;
import fr.cyu.cybooks.models.User;

import java.time.LocalDateTime;
import java.util.*;

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
            System.out.println("2. Book Search");
            System.out.println("3. Loan Management");
            System.out.println("4. Statistics");
            System.out.println("5. Quit");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> displayUserManagementMenu();
                case 2 -> searchBooks();
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
                System.out.println("1. Display Current Loans");
                System.out.println("2. Display Overdue Loans");
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
        List<Loan> currentLoans = user.getCurrentLoans();

        if (!currentLoans.isEmpty()) {
            System.out.println("Current loans for user: " + user.getFirstName() + " " + user.getLastName());
            for (Loan loan : currentLoans) {
                System.out.println(loan);
                System.out.println();
            }
        } else {
            System.out.println("No current loans found for user with ID: " + user.getId());
        }
    }

    private static void displayUserOverdueLoans(User user) {
        List<Loan> overdueLoans = user.getOverdueLoans();

        if (!overdueLoans.isEmpty()) {
            System.out.println("Overdue loans for user: " + user.getFirstName() + " " + user.getLastName());
            for (Loan loan : overdueLoans) {
                System.out.println(loan);
                System.out.println();
            }
        } else {
            System.out.println("No overdue loans found for user with ID: " + user.getId());
        }
    }

    private static void displayUserLoanHistory(User user) {
        List<Loan> loanHistory = user.getLoanHistory();

        if (!loanHistory.isEmpty()) {
            System.out.println("Loan history for user: " + user.getFirstName() + " " + user.getLastName());
            for (Loan loan : loanHistory) {
                System.out.println(loan);
                System.out.println();
            }
        } else {
            System.out.println("No loan history found for user with ID: " + user.getId());
        }
    }

    private static void pickFilters() {
        boolean searching = true;
        String title = null;
        String author = null;
        String date = null;
        String genre = null;

        bookApi.clearFilter();

        while (searching) {
            System.out.println("\n===== Choisissez un filtre =====");
            // Option 1: Choose by title
            if (title == null) {
                System.out.println("1. Choisir par titre");
            } else {
                System.out.println("1. Titre = " + title + " Choisir un autre titre");
            }

            // Option 2: Choose by author
            if (author == null) {
                System.out.println("2. Choisir par auteur");
            } else {
                System.out.println("2. Auteur = " + author + " Choisir un autre auteur");
            }

            // Option 3: Choose by date
            if (date == null) {
                System.out.println("3. Choisir par date");
            } else {
                System.out.println("3. Date = " + date + " Choisir une autre date");
            }
            // Option 4: Choose by genre
            if (genre == null) {
                System.out.println("4. Choisir par genre");
            } else {
                System.out.println("4. Genre = " + genre + " Choisir un autre genre");
            }
            System.out.println("5. Rechercher");
            System.out.println("6. Annuler");
            System.out.print("Choisissez une option : ");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> {
                    System.out.print("Entrez le titre : ");
                    title = scanner.nextLine(); // Capture the title input
                    bookApi.addFilter("title", title,1); // Add the title filter
                }
                case 2 -> {
                    System.out.print("Entrez l'auteur : ");
                    author = scanner.nextLine();
                    bookApi.addFilter("author", author,1);
                }
                case 3 -> {
                    System.out.print("Entrez la date (AAAA) : ");
                    date = scanner.nextLine();
                    bookApi.addFilter("date", date,1);
                }
                case 4 -> {
                    System.out.print("Entrez le genre : ");
                    genre = scanner.nextLine();
                    bookApi.addFilter("genre", genre,2);
                }
                case 5 -> searching = false;
                case 6 -> {
                    System.out.println("Recherche annulée.");
                    return;
                }
                default -> System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    public static void searchBooks() {
        System.out.println("\n===== Rechercher un livre =====");
        pickFilters();
        int choice = -1;
        List<Book> results = new ArrayList<>();
        while(true) {
            results = bookApi.searchBooksByMap();
            if (!results.isEmpty()) {
                System.out.println("\nNombre de resultat : "+bookApi.getMax()+", resultat de "+bookApi.getIndex()+" a "+(bookApi.getIndex()+ results.size() - 1));
                Book.display(results, false);
                if (bookApi.getIndex()+bookApi.getJump()<=bookApi.getMax()) {
                    System.out.println("11. page suivante");
                }
                if (bookApi.getIndex()>1) {
                    System.out.println("12. page precedente");
                }
                System.out.println("13. Annuler");
                choice = getUserChoice();

                if (choice == 11 && bookApi.getIndex()+bookApi.getJump()<=bookApi.getMax()) {
                    bookApi.setIndex(bookApi.getIndex()+bookApi.getJump());
                }else if (choice == 12 && bookApi.getIndex()>1) {
                    bookApi.setIndex(bookApi.getIndex()-bookApi.getJump());
                }else if(choice == 13){
                    return;
                }else{
                    System.out.println("Choisissez une option valide.");
                }
            } else {
                System.out.println("Aucun résultats trouvés");
                bookApi.clearFilter();
                return;
            }
        }
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
            pickFilters();
            List<Book> results;
            int choice = -1;
            while (true) {
                results = bookApi.searchBooksByMap();

                if (!results.isEmpty()) {
                    System.out.printf("Number of results: %d (from %d to %d)\n", bookApi.getMax(), bookApi.getIndex(), bookApi.getIndex() + results.size() - 1);
                    Book.display(results, true);

                    if (bookApi.getIndex()+bookApi.getJump()<=bookApi.getMax()) {
                        System.out.println("11. page suivante");
                    }
                    if (bookApi.getIndex()>1) {
                        System.out.println("12. page precedente");
                    }
                    System.out.println("13. Annuler");

                    choice = getUserChoice();

                    if (choice >= 1 && choice <= results.size()) {
                        break;
                    } else if (choice == 11 && bookApi.getIndex()+bookApi.getJump()<=bookApi.getMax()) {
                        bookApi.setIndex(bookApi.getIndex()+bookApi.getJump());
                    }else if (choice == 12 && bookApi.getIndex()>1) {
                        bookApi.setIndex(bookApi.getIndex()-bookApi.getJump());
                    }else if(choice == 13){
                        return;
                    }else{
                        System.out.println("Choisissez une option valide.");
                    }
                } else {
                    System.out.println("No books found.");
                }
            }

            Book book = results.get(choice-1);

            if (!book.isAvailableForLoan()) {
                System.out.println("This book is not available to loan.");
                return;
            }

            System.out.print("Borrowing time (in seconds) : ");
            int borrowingTime = scanner.nextInt();
            scanner.nextLine();

            Loan loan = new Loan(book, user, borrowingTime);

            if (loanDAO.create(loan)) {
                System.out.println("'" + book.getTitle() + "' added to the loan list of " + user.getFullName() + ".");
            } else {
                System.err.println("Error during the loan process.");
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
                loan.setReturnDate(LocalDateTime.now());

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
            System.out.println();
        }
    }

    public static void displayStatisticsMenu() {
        while (true) {
            System.out.println("\n=== Statistics ===");
            System.out.println("1. Top 10 Most Loaned Books (Last 30 Days)");
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

        Map<String, Integer> mostLoanedBooks = loanDAO.getMostLoanedBooks(30, 10);

        if (mostLoanedBooks.isEmpty()) {
            System.out.println("No books have been loaned yet.");
            return;
        }

        mostLoanedBooks.forEach((bookId, loanCount) -> System.out.println("Book ID: " + bookId + " | Loan Count: " + loanCount));
    }

    private static int getUserChoice() {
        int choice = -1;
        while (true) {
            try {
                System.out.print("\nMake your choice: ");
                choice = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return choice;
    }
}
