package fr.cyu.cybooks.view;
import fr.cyu.cybooks.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
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

public class ClientInfoController{

    private static final BookAPI bookApi = new BookAPI();
    private static final LoanDAO loanDAO = DAOFactory.getLoanDAO();
    private static final UserDAO userDAO = DAOFactory.getUserDAO();

    private Book book;

    @FXML
    private Button return_btn;

    @FXML
    private Button show_user_info_btn;

    @FXML
    private Button show_loan_btn;

    @FXML
    private Button show_return_loan_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private Label userFullName;



    @FXML
    private AnchorPane user_info_container;

    @FXML
    private AnchorPane set_edit_user;

    @FXML
    private TableColumn<?, ?> col_book_title;

    @FXML
    private TableColumn<?, ?> col_book_author;

    @FXML
    private TableColumn<?, String> col_book_genre;

    @FXML
    private TableColumn<?, ?> col_book_date;


    private ObservableList<Loan> list = FXCollections.observableArrayList();

    @FXML
    private Button edit_btn;

    @FXML
    private Label userId;

    @FXML
    private AnchorPane user_menu;

    @FXML
    private AnchorPane user_search_center_form;

    @FXML
    private AnchorPane book_search_container;

    @FXML
    private Button book_search_btn;

    @FXML
    private AnchorPane book_results_container;

    @FXML
    private Button next_btn;

    @FXML
    private Button prev_btn;

    @FXML
    private Button return_to_search;

    @FXML
    private AnchorPane user_split_center_form;

    @FXML
    private AnchorPane split_table_container;

    @FXML
    private AnchorPane book_loans_container;

    @FXML
    private AnchorPane user_loans_container;

    @FXML
    private TableView user_loan_table;


    @FXML
    private Tab user_overdue_loans;

    @FXML
    private Tab user_current_loans;

    @FXML
    private Tab user_history_loans;


    @FXML
    private TabPane current_user_loans_container;

    @FXML
    private AnchorPane split_info_container;

    @FXML
    private RadioButton user_currentloans_radio;

    @FXML
    private RadioButton user_overdueloans_radio;

    @FXML
    private RadioButton user_historyloans_radio;

    @FXML
    private AnchorPane loan_container;

    @FXML
    private AnchorPane return_loan_container;

    @FXML
    private AnchorPane set_time_loan;
    @FXML
    private CheckBox by_title;

    @FXML
    private TextField search_title;

    @FXML
    private CheckBox by_author;

    @FXML
    private TextField search_author;

    @FXML
    private CheckBox by_genre;

    @FXML
    private ChoiceBox search_genre;

    @FXML
    private CheckBox by_date;

    @FXML
    private TextField search_date;

    @FXML
    private Label book_search_sum;

    @FXML
    private ChoiceBox<Integer> set_pagination;

    @FXML
    private TableView book_search_res;
    @FXML
    private VBox filters_vbox;
    @FXML
    private AnchorPane book_split_center_form;

    @FXML
    private Label book_details;

    @FXML
    private Button return_to_res;

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
    private Button loan_btn;
    @FXML
    private TableView selected_book_table;
    @FXML
    private TextField loan_time;
    @FXML
    private Button ok_btn;
    @FXML
    private Label loan_error;
    @FXML
    private Button return_loan_btn;
    @FXML
    private AnchorPane user_loan_table_anchor;

//    @FXML
//    private Label userMail;

    private MainMenuController mainController;

    private Stage mainMenuStage; // Reference to the main menu stage


    private User user;


    @FXML
    private CheckBox edit_firstName;

    @FXML
    private TextField set_firstName;

    @FXML
    private CheckBox edit_lastName;

    @FXML
    private TextField set_lastName;

    @FXML
    private CheckBox edit_email;

    @FXML
    private TextField set_email;

    @FXML
    private Button update_btn;

    @FXML
    private Button cancel_update_btn;


    public void setMainController(MainMenuController mainController) {
        this.mainController = mainController;
    }


    public void setPrimaryStage(Stage mainMenuStage) {
        this.mainMenuStage = mainMenuStage;
    }

    private double x;
    private double y;

    @FXML
    public void logout(ActionEvent event){
        try{
            if(event.getSource()==logout_btn || event.getSource()==return_btn){
                showMainMenu();
            }
        }catch(Exception e){e.printStackTrace();}
    }
    public void setUser_info(User user){
        this.user=user;
        userId.setText(setUser_details(user));
        userFullName.setText(user.getFullName());
        initializeInputFieldLists();
        show_user_info_btn.setOnAction(event -> {
            setShow_user_info_btn();
        });
        user_current_loans.setOnSelectionChanged(event ->{
            setToCurrentLoans();
        });
        user_overdue_loans.setOnSelectionChanged(event ->{
            setToOverdueLoans();
        });
        user_history_loans.setOnSelectionChanged(event ->{
            setToHistoryLoans();
        });

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

    public void setShow_loan_btn(ActionEvent event){
        book_details.setVisible(true);
        initBookSearch();
    }

    public void setShow_return_loan_btn(ActionEvent event){
        showReturnLoan();
    }
    public void showReturnLoan(){
        user_split_center_form.setVisible(true);
        book_details.setVisible(true);
        loan_container.setVisible(false);
        user_info_container.setVisible(false);
        return_loan_container.setVisible(true);
        book_loans_container.setVisible(false);
        user_loans_container.setVisible(true);
        current_user_loans_container.setVisible(false);

        user_search_center_form.setVisible(false);



        updateCurrentLoan();


        selected_book_table.setPlaceholder(new Label("Aucun livre n'est emprunté"));

    }
    @FXML
    public void setShow_user_info_btn() {
        user_split_center_form.setVisible(true);

        loan_container.setVisible(false);
        user_info_container.setVisible(true);
        return_loan_container.setVisible(false);
        book_loans_container.setVisible(false);
        user_loans_container.setVisible(true);
        current_user_loans_container.setVisible(true);
        book_details.setVisible(false);
        user_search_center_form.setVisible(false);
        current_user_loans_container.getSelectionModel().select(user_current_loans);
        updateCurrentLoan();
    }
    @FXML
    public void setToCurrentLoans(){
        updateCurrentLoan();
    }
    public void setToOverdueLoans(){
        updateOverdueLoan();
        selected_book_table.setPlaceholder(new Label("Aucun emprunt en retard"));

    }
    public void setToHistoryLoans(){
        col_sel_loan_id.setCellValueFactory(new PropertyValueFactory<>("id"));


        col_sel_loan_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        col_sel_loan_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        col_sel_loan_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_sel_loan_due.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        col_sel_loan_date.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        col_sel_loan_return.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        updateHistoryLoan();

        selected_book_table.setPlaceholder(new Label("Aucun livre retourné"));

    }
    public void initBookSearch(){

        bookApi.clearFilter();
        clearSearchFields();
        book_results_container.setVisible(false);
        book_search_container.setVisible(true);

        if(user_split_center_form==null && user_search_center_form==null) {
            book_split_center_form.setVisible(false);
            book_results_container.setVisible(false);
            book_search_container.setVisible(true);
        }else{user_split_center_form.setVisible(false);
            user_search_center_form.setVisible(true);}

        ObservableList<String> genres = FXCollections.observableArrayList(
                "", "Action", "Thriller", "Romance", "Fiction", "Drama", "Sciences", "Politique", "Histoire"
        );
        search_genre.setItems(genres);


        if (set_pagination == null) {
            set_pagination = new ChoiceBox<>(FXCollections.observableArrayList(5, 10, 15, 20, 30, 50));
        } else {
            set_pagination.setItems(FXCollections.observableArrayList(5, 10, 15, 20, 30, 50));
        }
        search_genre.getSelectionModel().select(0);
        set_pagination.getSelectionModel().select(1);// Selects 10 by default
        col_book_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_book_author.setCellValueFactory(new PropertyValueFactory<>("author"));

        col_book_genre.setCellValueFactory(cellData ->{
            if(!by_genre.isSelected() || search_genre.getValue()==""){
                    return new SimpleStringProperty("");
            }else{
                return new SimpleStringProperty((String) search_genre.getValue());
            }
        });

        col_book_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        by_title.selectedProperty().addListener((observable, oldValue, newValue) -> search_title.setDisable(!newValue));
        by_author.selectedProperty().addListener((observable, oldValue, newValue) -> search_author.setDisable(!newValue));
        by_genre.selectedProperty().addListener((observable, oldValue, newValue) -> search_genre.setDisable(!newValue));
        by_date.selectedProperty().addListener((observable, oldValue, newValue) -> search_date.setDisable(!newValue));
        book_search_res.setPlaceholder(new Label(""));

    }
    @FXML
    private VBox edit_vbox;
    public void pickFilters(ActionEvent e){
        bookApi.setJump(set_pagination.getValue()); // Set the jump value based on the selected number of results per page

        bookApi.clearFilter(); // Clear previous filters

        boolean validInput = false;
        boolean onlyIndeterminate=true;

        if (!search_title.getText().isEmpty() && by_title.isSelected()) {
            bookApi.addFilter("title", search_title.getText(),1);
            validInput = true;
            onlyIndeterminate=false;
        }else if (!search_title.getText().isEmpty() && by_title.isIndeterminate()) {
            bookApi.addFilter("title", search_date.getText(),2);
            validInput = true;
        }
        if (!search_author.getText().isEmpty() && by_author.isSelected()) {
            bookApi.addFilter("author", search_author.getText(),1);
            validInput = true;
            onlyIndeterminate=false;
        }else if (!search_author.getText().isEmpty() && by_author.isIndeterminate()) {
            bookApi.addFilter("author", search_author.getText(),2);
            validInput = true;
        }
        if (!search_date.getText().isEmpty() && by_date.isSelected()) {
            bookApi.addFilter("date", search_date.getText(),1);
            validInput = true;
            onlyIndeterminate=false;
        } else if (!search_date.getText().isEmpty() && by_date.isIndeterminate()) {
            bookApi.addFilter("date", search_date.getText(),2);
            validInput = true;
        }

        String selectedGenre = (String) search_genre.getValue();
        if (selectedGenre != null && by_genre.isSelected()) {
            bookApi.addFilter("genre", selectedGenre, 1);
            validInput = true;
            onlyIndeterminate=false;
        } else if (selectedGenre != null && by_genre.isIndeterminate()) {
            bookApi.addFilter("genre", selectedGenre, 2);
            validInput = true;
        }

        if (!validInput) {
            MainMenuController.showAlert("Admin Message","Erreur : Veuillez entrer au moins un filtre.", Alert.AlertType.ERROR);
        }else{
            searchBooks();
        }


    }

    public void searchBooks(){
        book_search_container.setVisible(false);
        book_results_container.setVisible(true);
        List<Book> results = bookApi.searchBooksByMap();
        if (!results.isEmpty()) {


            book_search_sum.setText("\n" + bookApi.getMax() + " Résultats de " + bookApi.getIndex() + " à " + (bookApi.getIndex() + results.size() - 1));
            book_search_res.getItems().setAll(results);
            book_search_res.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


            book_search_res.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {

                    Book selectedBook = (Book) book_search_res.getSelectionModel().getSelectedItem();

                    setBook(selectedBook);
                    if(user_split_center_form==null && user_search_center_form==null) {

                        showBookDetails();
                    }else{

                        showLoan();
                    }

                }
            });

            next_btn.setOnAction(ActionEvent -> {
                if (bookApi.getIndex() + bookApi.getJump() <= bookApi.getMax()) {
                    bookApi.setIndex(bookApi.getIndex() + bookApi.getJump());
                    results.clear();
                    results.addAll(bookApi.searchBooksByMap());
                    prev_btn.setDisable(false);
                    book_search_res.getItems().setAll(results);
                }else{
                    next_btn.setDisable(true);
                }
            });
            prev_btn.setOnAction(ActionEvent -> {
                if (bookApi.getIndex() > 1) {
                    bookApi.setIndex(bookApi.getIndex() - bookApi.getJump());
                    results.clear();
                    results.addAll(bookApi.searchBooksByMap());
                    book_search_res.getItems().setAll(results);
                }else{
                    prev_btn.setDisable(true);
                }
            });

        }else{
            book_search_sum.setText("Aucun Résultat trouvé...");

        }
        return_to_search.setOnAction(ActionEvent -> {
            results.clear();
            bookApi.clearFilter();
            clearSearchFields();
            book_results_container.setVisible(false);
            book_search_container.setVisible(true);
        });
    }
    public void clearSearchFields(){
        for (Node node : filters_vbox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                Node graphic = checkBox.getGraphic();
                if (graphic instanceof TextField && !checkBox.isSelected()) {
                    TextField textField = (TextField) graphic;
                    textField.setText("");
                }
            }
        }
    }


    public void showLoan(){
        user_split_center_form.setVisible(true);

        loan_container.setVisible(true);
        user_info_container.setVisible(false);
        return_loan_container.setVisible(false);
        book_loans_container.setVisible(true);
        user_loans_container.setVisible(false);
        current_user_loans_container.setVisible(false);

        user_search_center_form.setVisible(false);




        col_sel_loan_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_sel_loan_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_sel_loan_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_sel_loan_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        col_sel_loan_date.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        col_sel_loan_due.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        col_sel_loan_return.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        selected_book_table.setPlaceholder(new Label("Ce livre n'est actuellement pas emprunté"));

        updateCurrentLoan(selected_book_table, book);


    }
    @FXML
    public void updateCurrentLoan(){
        col_sel_loan_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        col_sel_loan_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        col_sel_loan_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        col_sel_loan_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        col_sel_loan_date.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        col_sel_loan_due.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        col_sel_loan_return.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        selected_book_table.setPlaceholder(new Label("Aucun livre n'est emprunté"));

        list.clear();
        list.addAll(user.getCurrentLoans());
        selected_book_table.getItems().clear();
        if(!list.isEmpty()) {
            return_loan_btn.setVisible(true);
            if (selected_book_table==null){
                selected_book_table.setItems(list);
            }
            else{
                selected_book_table.getItems().setAll(list);
            }
            selected_book_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            if (selected_book_table.getSelectionModel().isEmpty()) {
                selected_book_table.getSelectionModel().selectFirst();
            }

            Loan loan = (Loan) selected_book_table.getSelectionModel().getSelectedItem();
            setBook((Book) loan.getBook());


        }else {
            book_details.setText("Aucun livre n'est emprunté");
            return_loan_btn.setVisible(false);
        }
    }
    public void updateOverdueLoan(){
        selected_book_table.getItems().clear();
        list.clear();
        list.addAll(user.getOverdueLoans());
        if(!list.isEmpty()){
            selected_book_table.getItems().setAll(list);
            selected_book_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            if(selected_book_table.getSelectionModel().isEmpty()){
                selected_book_table.getSelectionModel().selectFirst();
            }

            selected_book_table.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {

                    Loan loan= (Loan) selected_book_table.getSelectionModel().getSelectedItem();
                    setBook((Book) loan.getBook());
                    showReturnLoan();
                }
            });

        }
    }
    public void updateHistoryLoan(){
        selected_book_table.getItems().clear();
        list.clear();
        list.addAll(user.getLoanHistory());
        if(!list.isEmpty()){
            selected_book_table.getItems().setAll(list);
            selected_book_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
    }
    public void registerNewReturn(ActionEvent event){
        if(user!=null){
            Loan loan = (Loan) selected_book_table.getSelectionModel().getSelectedItem();
            loan.setReturnDate(LocalDateTime.now());
            if (loanDAO.update(loan)){
                MainMenuController.showAlert("Admin Message", "Book successfully returned", Alert.AlertType.INFORMATION);
                int row = selected_book_table.getSelectionModel().getSelectedIndex();

                if (row>0){
                    selected_book_table.getSelectionModel().selectPrevious();
                }
                selected_book_table.getItems().remove(row);
                updateCurrentLoan();

            }else{
                MainMenuController.showAlert("Admin Message", "Error returning book", Alert.AlertType.ERROR);
            }
        }else{
            MainMenuController.showAlert("Admin Message", "No user", Alert.AlertType.ERROR);

        }
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
            if(book_split_center_form==null){
                if(user==null){
                    MainMenuController.showAlert("Admin Message", "User null ERROR", Alert.AlertType.ERROR);
                }else {
                    if (!user.isEligibleToLoan()) {
                        loan_btn.setVisible(false);
                        loan_error.setVisible(true);
                        loan_error.setText("Utilisateur non éligible");
                    } else {
                        loan_btn.setVisible(true);
                        loan_error.setVisible(false);
                    }
                }
            }

        }
        return_to_res.setOnAction(ActionEvent -> {
            returnToResults();
        });
    }
    public void setBook(Book book){
        this.book=book;
        book_details.setText(book.getTitle());
    }
    public void registerNewLoan(ActionEvent event){
        loan_error.setVisible(false);

        try {
            if (user.isEligibleToLoan()) {
                set_time_loan.setVisible(true);
                ok_btn.setOnAction(ActionEvent -> {

                    int loanTime = Integer.parseInt(loan_time.getText());
                    Loan loan = new Loan(book, user, loanTime);
                    if (loanDAO.create(loan)) {
                        if(MainMenuController.showAlert("Admin Message", "'" + book.getTitle() + "' added to the loan list of " + user.getFullName() + ".", Alert.AlertType.INFORMATION)){
                            set_time_loan.setVisible(false);
                        };

                        updateCurrentLoan(selected_book_table, book);

                    } else {
                        MainMenuController.showAlert("Admin Message", "Error during the loan process.", Alert.AlertType.ERROR);

                    }
                });
            }else{
                loan_btn.setVisible(false);
                loan_error.setVisible(true);
                loan_error.setText("Utilisateur non éligible");
            }




        }catch(Exception e){
            e.printStackTrace();
        }


    }
    public void returnToResults(){

        if(user_search_center_form==null && user_split_center_form==null){

            book_results_container.setVisible(true);
            book_search_container.setVisible(false);
            book_split_center_form.setVisible(false);
        }else{
            user_search_center_form.setVisible(true);
            book_search_container.setVisible(false);
            book_results_container.setVisible(true);
            user_split_center_form.setVisible(false);
        }
    }

    public void showBookDetails(){
        book_split_center_form.setVisible(true);
        book_results_container.setVisible(false);
        book_search_container.setVisible(false);

        col_sel_loan_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_sel_loan_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_sel_loan_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_sel_loan_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_sel_loan_due.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        col_sel_loan_date.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        col_sel_loan_return.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        selected_book_table.setPlaceholder(new Label("Ce livre n'est actuellement pas emprunté"));

        loan_error.setText("Veuillez identifier un utilisateur pour emprunter ce livre");
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
    @FXML
    private List<CheckBox> checkboxes;

    @FXML
    private List<TextField> inputFields;
    @FXML
    private void initializeInputFieldLists() {
        checkboxes = edit_vbox.getChildren().stream()
                .filter(node -> node instanceof CheckBox)
                .map(CheckBox.class::cast)
                .collect(Collectors.toList());

        inputFields = checkboxes.stream()
                .map(checkBox -> (TextField) checkBox.getGraphic())
                .filter(graphic -> graphic instanceof TextField)
                .map(TextField.class::cast)
                .collect(Collectors.toList());

    }
    public void setShow_edit_btn(ActionEvent event) {
        set_edit_user.setVisible(true);
        initializeInputFieldLists();
        if (checkboxes.isEmpty() || inputFields.isEmpty()) {
            System.out.println("Initialization failed: Checkboxes or InputFields are empty");
            return;
        }
        update_btn.setOnAction(e -> {
            boolean anyCheckboxSelected = checkboxes.stream().anyMatch(CheckBox::isSelected);
            boolean anyCheckboxSelectedAndEmpty = IntStream.range(0, 3)
                    .anyMatch(i -> checkboxes.get(i).isSelected() && inputFields.get(i).getText().isEmpty());

            if (!anyCheckboxSelected) {
                // Si aucune case à cocher n'est sélectionnée

                MainMenuController.showAlert("Admin Message", "rien n'est selectionné", Alert.AlertType.INFORMATION);

            } else if (anyCheckboxSelectedAndEmpty) {
                // Si au moins une case à cocher est sélectionnée et que les champs d'entrée correspondants sont vides

                MainMenuController.showAlert("Admin Message", "aucune modification", Alert.AlertType.INFORMATION);

            } else {
                // Si au moins une case à cocher est sélectionnée et que les champs d'entrée correspondants ne sont pas vides
                IntStream.range(0, 3)
                        .filter(i -> checkboxes.get(i).isSelected())
                        .forEach(i -> {
                            switch (i) {
                                case 0:
                                    user.setFirstName(inputFields.get(i).getText());
                                    break;
                                case 1:
                                    user.setLastName(inputFields.get(i).getText());
                                    break;
                                case 2:

                                    user.setEmail(inputFields.get(i).getText());

                                    break;
                                // Add cases for other input fields
                            }
                        });

                boolean success = userDAO.update(user);

                if (success) {
                    MainMenuController.showAlert("Admin message", "User edited successfully.", Alert.AlertType.INFORMATION);
                    set_edit_user.setVisible(false);
                    inputFields.forEach(textField -> textField.clear());
                    checkboxes.forEach(checkBox -> checkBox.setSelected(false));
                    userId.setText(setUser_details(user));
                } else {
                    MainMenuController.showAlert("Admin message", "Failed to edit user.", Alert.AlertType.INFORMATION);
                }

            }
        });
    }

    public void setCancel_update_btn(ActionEvent event){
        set_firstName.clear();
        set_email.clear();
        set_lastName.clear();
        set_edit_user.setVisible(false);
    }
    public String setUser_details(User user){
        return "ID: " + user.getId() + "\n\n" +
                "Firstname: " + user.getFirstName() + "\n\n" +
                "Lastname: " + user.getLastName() + "\n\n" +
                "Email: " + user.getEmail();
    }

}

