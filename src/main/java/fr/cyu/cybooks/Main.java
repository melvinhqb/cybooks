package fr.cyu.cybooks;

import fr.cyu.cybooks.dao.DAOFactory;
import fr.cyu.cybooks.dao.api.BookAPI;
import fr.cyu.cybooks.dao.implement.LoanDAO;
import fr.cyu.cybooks.dao.implement.UserDAO;
import fr.cyu.cybooks.models.Book;
import fr.cyu.cybooks.models.Loan;
import fr.cyu.cybooks.models.User;

import java.time.LocalDateTime;
import java.io.IOException;
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
                case 4 -> handleUser();
                case 5 -> Create();
                case 0 -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void Create() {
        if (user==null){
            createUser();
        } else{
            System.out.println("Choix invalide. Veuillez réessayer.");
        }
    }

    private static void handleUser() {
        if (user!=null){
            disconnectUser();
        } else{
            user = connectUser();
        }
    }

    private static void disconnectUser() {
        if (user != null) {
            user = null;
        }
    }

    private static int showMainMenu() {
        if(user != null) {
            System.out.println(" - Connecté en tant que " + user.getFullName());
        }
        System.out.println("\n===== Menu Principal =====");
        System.out.println("1. Emprunt de livre");
        System.out.println("2. Retour de livre");
        System.out.println("3. Infos Client");
        if(user != null) {
            System.out.println("4. Deconnection Client");
        }else{
            System.out.println("4. Connecter Client");
            System.out.println("5. Cree Client");
        }
        System.out.println("0. Quitter");
        System.out.print("Choisissez une option: ");
        return getUserChoice();
    }


    private static void borrowBook() {

        boolean rightBook = false;

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
            pickFilters();
            int choice = -1;
            List<Book> results = new ArrayList<>();
            while(!rightBook) {
                results = bookApi.searchBooksByMap();
                if (!results.isEmpty()) {
                    System.out.println("\nNombre de resultat : "+bookApi.getMax()+", resultat de "+bookApi.getIndex()+" a "+(bookApi.getIndex()+ results.size() - 1));
                    Book.display(results, true);
                    if (bookApi.getIndex()+bookApi.getJump()<=bookApi.getMax()) {
                        System.out.println("11. page suivente");
                    }
                    if (bookApi.getIndex()>1) {
                        System.out.println("12. page precedente");
                    }
                    System.out.println("13. Annuler");
                    System.out.print("Choisir : ");
                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice >= 1 && choice <= results.size()) {
                        rightBook = true;
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
                    System.out.println("Aucun résultats trouvés");
                    bookApi.clearFilter();
                    return;
                }
            }

            Book book = results.get(choice - 1);
            Loan loan = new Loan(book, user);

            if (loanDAO.create(loan)) {
                System.out.println("'" + book.getTitle() + "' ajouté à la liste d'emprunts de " + user.getFullName());
            } else {
                System.err.println("Erreur lors du processus d'emprunt.");
            }
        }


        String response;
        System.out.println("Se déconnecter du profil de " + user.getFullName() + "? (oui/non)");
        response = scanner.nextLine();
        if (response.equalsIgnoreCase("oui")) {
            user = null;
        }
    }

    private static void pickFilters() {
        boolean searching = true;
        String title = null;
        String author = null;
        String date = null;
        String genre = null;

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
            scanner.nextLine(); // Consume the leftover newline character
            switch (choice) {
                case 1:
                    System.out.print("Entrez le titre : ");
                    title = scanner.nextLine(); // Capture the title input
                    bookApi.addFilter("title", title,1); // Add the title filter
                    break;
                case 2:
                    System.out.print("Entrez l'auteur : ");
                    author = scanner.nextLine();
                    bookApi.addFilter("author", author,1);
                    break;
                case 3:
                    System.out.print("Entrez la date (AAAA) : ");
                    date = scanner.nextLine();
                    bookApi.addFilter("date", date,1);
                    break;
                case 4:
                    System.out.print("Entrez le genre : ");
                    genre = scanner.nextLine();
                    bookApi.addFilter("genre", genre,1);
                    break;
                case 5:
                    searching = false;
                    break;
                case 6:
                    System.out.println("Recherche annulée.");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
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
