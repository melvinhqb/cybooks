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

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = DAOFactory.getUserDAO();
    private static final LoanDAO loanDAO = DAOFactory.getLoanDAO();
    private static final BookAPI bookApi = new BookAPI();
    private static User user = null;

    public static void main(String[] args) {
        while (true) {
            switch (showMainMenu()) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> showClientInfo();
                case 0 -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static int showMainMenu() {
        System.out.println("\n===== Menu Principal =====");
        System.out.println("1. Emprunt de livre");
        System.out.println("2. Retour de livre");
        System.out.println("3. Infos Client");
        System.out.println("0. Quitter");
        System.out.print("Choisissez une option: ");
        return getUserChoice();
    }

    private static void borrowBook() {

        if (user == null) {
            user = connectUser();
        }

        if (user == null) {
            System.out.println("Le client n'est pas enregistré dans la base de données.");
            String response;
            do {
                System.out.print("Souhaitez-vous créer un nouveau client ? (oui/non) ");
                response = scanner.nextLine();
            } while (!response.equalsIgnoreCase("oui") && !response.equalsIgnoreCase("non"));
            if (response.equalsIgnoreCase("oui")) {
                user = createUser();
            } else {
                return;
            }
        }

        System.out.println("Connecté en tant que " + user.getFullName());

        if (!user.isEligibleToLoan()) {
            System.out.println("Ce client n'est pas éligible à l'emprunt.");
            return;
        } else {
            System.out.println("\n===== Rechercher un livre =====");
            System.out.print(">>> ");
            scanner.nextLine();
            bookApi.addFilter("title", scanner.nextLine());
            List<Book> results = bookApi.searchBooksByMap();
            if (!results.isEmpty()) {
                Book.display(results, true);
                System.out.print("Choisir : ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                // Récupérer le livre souhaité
                Book book = results.get(choice-1);
                Loan loan = new Loan(book, user);

                if(loanDAO.create(loan)) {
                    System.out.println("'" + book.getTitle() + "' ajouté à la liste d'emprunts de " + user.getFullName());
                } else {
                    System.err.println("Erreur lors du processus d'emprunt.");
                }
            } else {
                System.out.println("Aucun résultats trouvés");
            }
        }


        String response;
        System.out.println("Se déconnecter du profil de " + user.getFullName() + "? (oui/non)");
        response = scanner.nextLine();
        if (response.equalsIgnoreCase("oui")) {
            user = null;
        }
    }

    private static User connectUser() {
        System.out.println("\n===== Portail de connexion =====");
        return userDAO.find(getUserId());
    }

    private static User createUser() {
        System.out.println("\n===== Nouveau Client =====");
        user = new User();
        System.out.print("NOM: ");
        user.setLastName(scanner.nextLine());
        System.out.print("PRÉNOM: ");
        user.setFirstName(scanner.nextLine());
        System.out.print("EMAIL: ");
        user.setEmail(scanner.nextLine());
        userDAO.create(user);
        return user;
    }

    private static int getUserId() {
        System.out.print("Saisir ID client : ");
        while (!scanner.hasNextInt()) {
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void returnBook() {

        if (user == null) {
            user = connectUser();
        }

        if (user != null) {
            // Afficher la liste des livres empruntés par le client
            System.out.println("Liste des livres empruntés par " + user.getFullName() + ": ");
            List<Loan> loansList = user.getCurrentLoans();
            List<Book> bookList = new ArrayList<>();
            for (Loan loan : loansList) {
                bookList.add(loan.getBook());
            }
            Book.display(bookList, true);
            System.out.print("Choisir : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Récupérer le livre souhaité
            Loan loan = loansList.get(choice-1);
            loan.setReturnDate(LocalDateTime.now());
            loanDAO.update(loan);
        }

        String response;
        assert user != null;
        System.out.println("Se déconnecter du profil de " + user.getFullName() + "? (oui/non)");
        response = scanner.nextLine();
        if (response.equalsIgnoreCase("oui")) {
            user = null;
        }
    }

    private static void showClientInfo() {

        if (user == null) {
            user = connectUser();
        }

        if (user != null) {
            System.out.println(user);
            List<Book> currentLoans = user.getCurrentBorrowedBooks();
            if (currentLoans.isEmpty()) {
                System.out.println("Aucun livres empruntés");
            } else {
                System.out.println("\nListe des livres empruntés");
                Book.display(currentLoans);
            }
        } else {
            System.out.println("Le client n'est pas enregistré dans la base de données.");
        }
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
