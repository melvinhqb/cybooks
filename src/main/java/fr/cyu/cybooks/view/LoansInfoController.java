package fr.cyu.cybooks.view;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.cyu.cybooks.dao.api.BookAPI;
import fr.cyu.cybooks.dao.DAOFactory;
import fr.cyu.cybooks.dao.implement.LoanDAO;
import fr.cyu.cybooks.dao.implement.UserDAO;
import fr.cyu.cybooks.models.Book;
import fr.cyu.cybooks.models.Loan;
import fr.cyu.cybooks.models.User;
import com.sun.tools.javac.Main;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoansInfoController {
    private static final LoanDAO loanDAO = DAOFactory.getLoanDAO();
    private static final UserDAO userDAO = DAOFactory.getUserDAO();
    private static final BookAPI bookApi = new BookAPI();

    @FXML
    private Button return_btn;

    @FXML
    private Label consult;

    @FXML
    private AnchorPane center_form;

    @FXML
    private AnchorPane loaned_books;

    @FXML
    private Label book_search_sum;

    @FXML
    private TableView loan_table;

    @FXML
    private TableColumn<?, ?> col_loan_id;

    @FXML
    private TableColumn<?, ?> col_loan_userId;

    @FXML
    private TableColumn<?, ?> col_loan_title;

    @FXML
    private TableColumn<?, ?> col_loan_author;

    @FXML
    private TableColumn<?, ?> col_loan_loanDate;


    @FXML
    private TableColumn<?, ?> col_loan_dueDate;

    @FXML
    private TableColumn<?, ?> col_loan_returnDate;
    @FXML
    private Label list_popular;

    @FXML
    private Label list_consultation;
    @FXML
    private AnchorPane popular_books;

    @FXML
    private TableView book_table;

    @FXML
    private TableColumn<?, ?> col_book_title;

    @FXML
    private TableColumn<?, ?> col_book_author;
    @FXML
    private TableColumn<?, ?> col_book_date;

    @FXML
    private TableColumn<?, ?> col_book_count;

    @FXML
    private AnchorPane split_center_form;

    @FXML
    private AnchorPane split_table_container;

    @FXML
    private AnchorPane book_loans_container;

    @FXML
    private TableView<?> selected_book_table;

    @FXML
    private TableColumn<?, ?> col_sel_loan_id;

    @FXML
    private TableColumn<?, ?> col_sel_loan_userId;
    @FXML
    private TableColumn<?, ?> col_sel_loan_title;

    @FXML
    private TableColumn<?, ?> col_sel_loan_author;

    @FXML
    private TableColumn<?, ?> col_sel_loan_date;

    @FXML
    private TableColumn<?, ?> col_sel_loan_due;

    @FXML
    private TableColumn<?, ?> col_sel_loan_return;


    @FXML
    private Button return_to_res;

    @FXML
    private AnchorPane split_info_container;

    @FXML
    private AnchorPane loan_container;

    @FXML
    private Label loan_error;

    @FXML
    private Button loan_btn;

    @FXML
    private Label book_details;
    private MainMenuController mainController;

    private Stage mainMenuStage; // Reference to the main menu stage


    private User user;


    public void setMainController(MainMenuController mainController) {
        this.mainController = mainController;
    }


    public void setPrimaryStage(Stage mainMenuStage) {
        this.mainMenuStage = mainMenuStage;
    }

    @FXML
    public void logout(ActionEvent event) {
        try {

            showMainMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController menuController = loader.getController();


        menuController.setMainController(mainController);
        return_btn.getScene().getWindow().hide();

        Scene mainMenuScene = new Scene(root);

        menuController.setPrimaryStage(mainMenuStage);

        mainMenuStage.setScene(mainMenuScene);
        mainMenuStage.show();
    }
    private Button nextButton;
    private Button prevButton;
    public void showAllLoans() {
        center_form.setVisible(true);
        split_center_form.setVisible(false);
        loaned_books.setVisible(true);
        popular_books.setVisible(false);
        list_consultation.setText("Liste de Tous les Emprunts");

        loan_table.setPlaceholder(new Label("Aucun livre n'est actuellement emprunté"));

        col_loan_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_loan_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        col_loan_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        col_loan_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_loan_loanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        col_loan_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        col_loan_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));


        loan_table.getItems().clear();

        List<Loan> allCurrentLoans = loanDAO.getCurrentLoans();
        int pageSize = 10;
        int totalLoans = allCurrentLoans.size();
        int pageCount = (int) Math.ceil((double) totalLoans / pageSize);

        pagination.setPageCount(pageCount);

        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateLoanTable(allCurrentLoans, newValue.intValue(), pageSize);
            }
        });

        // Initial table update
        updateLoanTable(allCurrentLoans, pagination.getCurrentPageIndex(), pageSize);
    }

    private void updateLoanTable(List<Loan> allCurrentLoans, int currentPage, int pageSize) {
        int totalLoans = allCurrentLoans.size();
        int startIndex = currentPage * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalLoans);

        ObservableList<Loan> subList = FXCollections.observableArrayList();
        if (startIndex < totalLoans) {
            subList.addAll(allCurrentLoans.subList(startIndex, endIndex));
        }

        loan_table.getItems().setAll(subList);
        loan_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Enable or disable next and previous buttons based on the current page index
        pagination.setDisable(totalLoans <= pageSize);
    }

    // Other methods and fields



    public void showOverdueLoans() {
        center_form.setVisible(true);
        split_center_form.setVisible(false);
        loaned_books.setVisible(true);
        popular_books.setVisible(false);

        list_consultation.setText("Liste de Tous les Retards");

        loan_table.setPlaceholder(new Label("Aucun emprunt n'est en retard !"));

        col_loan_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_loan_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        col_loan_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        col_loan_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_loan_loanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        col_loan_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        col_loan_returnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        loan_table.getItems().clear();
        List<Loan> allOverdueLoans = loanDAO.getOverdueLoans();
        int pageSize = 10;
        int totalLoans = allOverdueLoans.size();
        int pageCount = (int) Math.ceil((double) totalLoans / pageSize);

        pagination.setPageCount(pageCount);

        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateLoanTable(allOverdueLoans, newValue.intValue(), pageSize);
            }
        });

        // Initial table update
        updateLoanTable(allOverdueLoans, pagination.getCurrentPageIndex(), pageSize);

    }

    @FXML
    Pagination pagination;
    public void showMostLoans() {
        center_form.setVisible(true);
        split_center_form.setVisible(false);
        loaned_books.setVisible(false);
        popular_books.setVisible(true);

        list_popular.setText("Liste des 10 Livres les Plus Empruntés les 30 Derniers Jours");

        col_book_date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        col_book_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        col_book_author.setCellValueFactory(new PropertyValueFactory<>("Author"));


            Map<String, Integer> mostLoanedBooksMap = loanDAO.getMostLoanedBooks(30,10);
            ObservableList<Book> bookList = FXCollections.observableArrayList();

            // Fetch the Book objects for each book ID and add them to the book list
            for (String bookId : mostLoanedBooksMap.keySet()) {
                Book book = bookApi.fetchBookById(bookId); // Using fetchBookById method
                if (book != null) {
                    bookList.add(book);
                }
            }

            // Set the items for the popular_books TableView
            book_table.setItems(bookList);

            // Set up the custom cell value factory for the loan count column
            col_book_count.setCellValueFactory(cellDataFeatures -> {
                Book book = (Book) cellDataFeatures.getValue(); // Get the Book object for the current cell
                int loanCount = mostLoanedBooksMap.get(book.getId()); // Get the loan count for this book from the map
                return new ReadOnlyObjectWrapper(loanCount); // Wrap the loan count in a ReadOnlyObjectWrapper and return it
            });

            book_table.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {

                    Book selectedBook = (Book) book_table.getSelectionModel().getSelectedItem();
                    showBookDetails(selectedBook);
                }
            });
    }

    public void showBookDetails(Book book){
        center_form.setVisible(false);
        split_center_form.setVisible(true);

        col_sel_loan_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_sel_loan_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_sel_loan_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_sel_loan_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_sel_loan_date.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        col_sel_loan_due.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        col_sel_loan_return.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        selected_book_table.setPlaceholder(new Label("Ce livre n'est actuellement pas emprunté"));

        loan_error.setText("Veuillez identifier un user pour emprunter ce livre");
        loan_btn.setStyle("-fx-opacity:60%; -fx-cursor:pointer;");
        updateCurrentLoan(selected_book_table, book);



        loan_btn.setOnAction(ActionEvent->{
            if(MainMenuController.showAlert("Admin Message", "Voulez vous identifier un user ?", Alert.AlertType.CONFIRMATION)) {
                try {
                    mainController.showLoginScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        });
    }

    public void updateCurrentLoan(TableView table, Book book){
        table.getItems().clear();
        ObservableList<Loan> currentLoans = FXCollections.observableArrayList();
        currentLoans.addAll(book.getCurrentLoans());
        if (!currentLoans.isEmpty()) {
            table.getItems().setAll(currentLoans);
        }

        if(!book.isAvailableForLoan()){
            loan_btn.setVisible(false);
            loan_error.setText("Il ne reste plus d'exemplaires de ce livre");
            loan_error.setVisible(true);

        }else{
            loan_btn.setVisible(true);

        }
        return_to_res.setOnAction(ActionEvent -> {
            center_form.setVisible(true);
            split_center_form.setVisible(false);
        });
    }
}
