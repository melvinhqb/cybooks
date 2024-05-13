package fr.cyu.cybooks;

import fr.cyu.cybooks.connection.DatabaseConnection;
import fr.cyu.cybooks.dao.DAO;
import fr.cyu.cybooks.dao.DAOFactory;
import fr.cyu.cybooks.dao.api.BookAPI;
import fr.cyu.cybooks.dao.implement.LoanDAO;
import fr.cyu.cybooks.dao.implement.UserDAO;
import fr.cyu.cybooks.models.Book;
import fr.cyu.cybooks.models.Loan;
import fr.cyu.cybooks.models.User;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        User user1 = new User("Melvin", "HQB", "rimelaas@gmail.com");
        UserDAO userDao = DAOFactory.getUserDAO();
        userDao.create(user1);

        BookAPI bookApi = new BookAPI();
        List<Book> bookList = bookApi.searchBooksByKeyword("harry potter et la chambre des secrets");
        Book book1 = bookList.get(0);

        Loan loan1 = new Loan(book1, user1);
        LoanDAO loanDAO = DAOFactory.getLoanDAO();
        loanDAO.create(loan1);
        loanDAO.all();

        System.out.println(DAOFactory.getLoanDAO().find(1));

        UserDAO userDAO = DAOFactory.getUserDAO();
        List<User> listUsers = userDAO.all();

        for (User user : listUsers) {
            System.out.println(user);
        }
    }

}
