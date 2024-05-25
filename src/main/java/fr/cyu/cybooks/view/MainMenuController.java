package fr.cyu.cybooks.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.layout.GridPane;
import fr.cyu.cybooks.dao.DAOFactory;
import fr.cyu.cybooks.dao.implement.UserDAO;
import fr.cyu.cybooks.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainMenuController implements Initializable{

    @FXML
    private Button showAllLoans_btn;

    @FXML
    private Button showAllOverdue_btn;

    @FXML
    private Button user_instance;

    @FXML
    private Button searchBook_btn;

    @FXML
    private Button allMostLoaned_btn;

    @FXML
    private TextField user_res_id;

    @FXML
    private Button quit_btn;

    @FXML
    private TextField user_res_firstname;

    @FXML
    private TextField user_res_lastname;

    @FXML
    private TextField user_res_mail;


    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene loginScene;
    private Scene allLoansScene;
    private Scene allOverdueScene;
    private Scene mostLoanedScene;
    private Scene userScene;

    private MainMenuController mainController;


    private Stage mainMenuStage; // Reference to the main menu stage

    public void setMainController(MainMenuController mainController) {
        this.mainController = mainController;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



//int id, String firstName, String lastName, String email
    @FXML
    public void showUserScene(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientInfo.fxml"));
        Parent root = loader.load();
        ClientInfoController userController = loader.getController();
        userController.setUser_info(user);
        userController.setShow_user_info_btn();
        userController.setMainController(mainController);

        // userController.setUser_info(id, firstName, lastName, email);

        closeStages();
     //   Stage loginStage = (Stage) login_btn.getScene().getWindow();
     //   loginStage.close();
        userScene = new Scene(root);
        Stage stage = new Stage();
        userController.setPrimaryStage(stage);

    //    userController.setPrimaryStage(primaryStage);
        primaryStage.setScene(userScene);

        primaryStage.show();

    }
    public void showLoginScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLogin.fxml"));
        Parent root = loader.load();
        loginScene = new Scene(root);
        MainMenuController loginController = loader.getController();
        loginController.setMainController(mainController);
        loginController.setPrimaryStage(primaryStage);
        Stage secondaryStage= new Stage();
        secondaryStage.setScene(loginScene);
        secondaryStage.initStyle(StageStyle.TRANSPARENT);
        secondaryStage.show();
    }

    public void showBookSearchScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchBook.fxml"));
        Parent root = loader.load();
        Scene bookSearchScene = new Scene(root);
        ClientInfoController bookSearchController = loader.getController();
        primaryStage.close();
        Stage stage = new Stage();
        bookSearchController.setMainController(mainController);
        bookSearchController.setPrimaryStage(stage);
        stage.setScene(bookSearchScene);
        bookSearchController.initBookSearch();
        stage.show();

    }
    public void showAllLoansScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoansInfo.fxml"));
        Parent root = loader.load();
        Scene bookSearchScene = new Scene(root);
        LoansInfoController loansInfoController = loader.getController();
        loansInfoController.showAllLoans();
        primaryStage.close();
        Stage stage = new Stage();
        loansInfoController.setMainController(mainController);
        loansInfoController.setPrimaryStage(stage);
        stage.setScene(bookSearchScene);

        stage.show();

    }
    public void showOverdueLoansScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoansInfo.fxml"));
        Parent root = loader.load();
        Scene bookSearchScene = new Scene(root);
        LoansInfoController loansInfoController = loader.getController();
        loansInfoController.showOverdueLoans();
        primaryStage.close();
        Stage stage = new Stage();
        loansInfoController.setMainController(mainController);
        loansInfoController.setPrimaryStage(stage);
        stage.setScene(bookSearchScene);

        stage.show();

    }
    public void showMostLoanedScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoansInfo.fxml"));
        Parent root = loader.load();
        Scene bookSearchScene = new Scene(root);
        LoansInfoController loansInfoController = loader.getController();
        loansInfoController.showMostLoans();
        primaryStage.close();
        Stage stage = new Stage();
        loansInfoController.setMainController(mainController);
        loansInfoController.setPrimaryStage(stage);
        stage.setScene(bookSearchScene);

        stage.show();

    }


    // Initialize the scenes
    public void initScenes() {
        try{

            Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            mainMenuScene = new Scene(mainMenuRoot);



            Parent allLoansRoot = FXMLLoader.load(getClass().getResource("AllLoans.fxml"));
            allLoansScene = new Scene(allLoansRoot);

            Parent allOverdueRoot = FXMLLoader.load(getClass().getResource("AllOverdue.fxml"));
            allOverdueScene = new Scene(allOverdueRoot);

            Parent mostLoanedRoot = FXMLLoader.load(getClass().getResource("MostLoaned.fxml"));
            mostLoanedScene = new Scene(mostLoanedRoot);

        }catch(Exception e){
            e.getStackTrace();
        }
    }


    @FXML
    public void setUser_instance(ActionEvent event){
        try{
            showLoginScene();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources){};


    @FXML
    private Label user_search_link;

    @FXML
    private Button signin_btn;

    @FXML
    private Button login_btn;


    @FXML
    private Button close_btn;

    private static final UserDAO userDAO = DAOFactory.getUserDAO();


    @FXML
    private void setClose_btn(MouseEvent event){
        try{
            Stage stage = (Stage) close_btn.getScene().getWindow();
            stage.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void login(ActionEvent event) {

        try {
            String userIdStr = user_res_id.getText();
            try {
                int userId = Integer.parseInt(userIdStr);
                User user = userDAO.find(userId);
                if (userIdStr.isEmpty()) {
                    showAlert("Admin message", "Please fill in the blanks", Alert.AlertType.ERROR);
                } else {
                    if (user != null) {
                        if(showAlert("Admin message", "User found: \n\n" + user.toString(), Alert.AlertType.INFORMATION)){


                        showUserScene(user);
                    }// showUserScene(userId, user.getFirstName(), user.getLastName(), user.getEmail());


                    } else {
                        showAlert("Admin Message", "No user found with ID: " + userId, Alert.AlertType.ERROR);
                    }
                }
            } catch (NumberFormatException e){
                showAlert("Invalid Input", "Please enter a valid user ID", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    public void registerNewUser(){
        try{
            String userFirstName = user_res_firstname.getText();
            String userLastName = user_res_lastname.getText();
            String userMail = user_res_mail.getText();
            if (userFirstName.isEmpty() || userLastName.isEmpty() || userMail.isEmpty()){
                showAlert("Admin Message", "Fill in the blanks", Alert.AlertType.ERROR);
            }else {
                User newUser = new User(0, userFirstName, userLastName, userMail);

                boolean success = userDAO.create(newUser);

                if (success) {
                    if(showAlert("Admin Message", "L'utilisateur a bien été ajouté avec l'id" + newUser.getId(), Alert.AlertType.INFORMATION)){;
                    showUserScene(newUser);
                    }
                } else {
                    showAlert("Admin Message", "Erreur d'enregistrement", Alert.AlertType.ERROR);
                }
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
    }

    private void closeStages() {
        if (primaryStage != null) {
            primaryStage.close();
        }
        Stage loginStage = (Stage) close_btn.getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void showSignInScene(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserSignin.fxml"));
            Parent root = loader.load();

            MainMenuController signInController = loader.getController();
            signInController.setMainController(mainController);
            signInController.setPrimaryStage(primaryStage);

            login_btn.getScene().getWindow().hide();

            Stage stage = new Stage();
            loginScene = new Scene(root);

            stage.setScene(loginScene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void userSearch(MouseEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserSearch.fxml"));
            Parent root = loader.load();
            MainMenuController userSearchController = loader.getController();
            userSearchController.setMainController(mainController);
            userSearchController.setPrimaryStage(primaryStage);

            login_btn.getScene().getWindow().hide();

            Stage stage = new Stage();
            loginScene = new Scene(root);

            stage.setScene(loginScene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static boolean showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    private GridPane userGrid;

    @FXML
    private Button user_search_btn;

    @FXML
    private TextField searchTerm;
    @FXML
    private Label user_search_res;

    @FXML
    public void initializeSearch(ActionEvent event) {

        searchUsers(searchTerm.getText());
    }
    @FXML
    private void searchUsers(String searchTerms) {

        userGrid.getChildren().clear(); // Clear previous results

        List<User> results = userDAO.search(searchTerms);

        if (!results.isEmpty()) {
            user_search_res.setText("Résultats trouvés pour : "+ searchTerms);
            int row = 0;
            int col = 0;
            int columns = 3; // Number of columns in the grid

            for (int i = 0; i < results.size(); i++) {
                User user = results.get(i);
                Label userLabel = new Label(user.toString());
                userLabel.setStyle("-fx-background-color: rgba(190,174,255,0.46); " +
                        "-fx-padding: 5px;"+
                        "-fx-font-family: Verdana;"+
                        "-fx-cursor: hand;"+
                        "-fx-background-radius: 5px;"
                        );
                userLabel.setOnMousePressed(mouseEvent -> {
                    try {
                        if(showAlert("Admin message", "Vous connecter en tant que: " + user.getId()+" ?", Alert.AlertType.CONFIRMATION)){
                            handleUserSelection(user);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                userGrid.add(userLabel, col, row);

                col++;
                if (col == columns) {
                    col = 0;
                    row++;
                }

            }
        } else {
            user_search_res.setText("Aucun résultats trouvés pour : "+ searchTerms);
        }
    }
    @FXML
    public void clearSearchTerm(ActionEvent event){
        user_search_res.setText("");
        searchTerm.setText("");
        userGrid.getChildren().clear();
    }

    @FXML
    public void handleUserSelection(User user) throws IOException {
        Stage stage = (Stage) user_search_btn.getScene().getWindow();
        stage.close();
        showUserScene(user);
    }
    public void setQuit_btn(ActionEvent event){
        System.exit(0);
    }
}
