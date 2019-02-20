package ca.ualberta.ishelf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class FirebaseTest {

    @Test
    public void getUser() {
        Firebase fb = new Firebase();

        // add a user
        String username = "testUsername";
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setRating(4);
        fb.addUser(testUser);

        User fbUser = fb.getUser(username);
        Assert.assertEquals(testUser, fbUser);

        fb.deleteUser(username);
    }

    @Test
    public void getBook() {
        Firebase fb = new Firebase();

        // add a book
        Book testBook = new Book();
        testBook.setName("testBook");
        testBook.setISBN("1234567890");
        String bookID = testBook.getID();
        fb.addBook(testBook);

        User fbBook = fb.getBook(bookID);
        Assert.assertEquals(testBook, fbBook);

        fb.deleteBook(bookID);
    }

    @Test
    public void getBooks() {
        Firebase fb = new Firebase();

        // add the first book
        Book testBook1 = new Book();
        testBook1.setName("testBook1");
        testBook1.setISBN("1234567890");
        String bookID1 = testBook1.getID();
        fb.addBook(testBook1);

        // add a second book
        Book testBook2 = new Book();
        testBook2.setName("testBook2");
        testBook2.setISBN("5432109876");
        String bookID2 = testBook2.getID();
        fb.addBook(testBook2);

        // set expected result
        ArrayList<Book> listOfBooks = new ArrayList<>();
        listOfBooks.add(testBook1);
        listOfBooks.add(testBook2);

        ArrayList<Book> fbListOfBooks = fb.getBooks();
        Assert.assertEquals(listOfBooks, fbListOfBooks);

        fb.deleteBook(bookID1);
        fb.deleteBook(bookID2);
    }

    @Test
    public void getAvailableBooks() {
        Firebase fb = new Firebase();

        // add borrowed book
        Book testBook1 = new Book();
        testBook1.setName("testBook1");
        testBook1.setISBN("1234567890");
        testBook1.setBorrowed();
        String bookID1 = testBook1.getID();
        fb.addBook(testBook1);

        // add available book
        Book testBook2 = new Book();
        testBook2.setName("testBook2");
        testBook2.setISBN("5432109876");
        testBook2.setAvailable();
        String bookID2 = testBook2.getID();
        fb.addBook(testBook2);

        // set expected result
        ArrayList<Book> listOfBooks = new ArrayList<>();
        listOfBooks.add(testBook2);

        ArrayList<Book> fbListOfBooks = fb.getAvailableBooks();
        Assert.assertEquals(listOfBooks, fbListOfBooks);

        fb.deleteBook(bookID1);
        fb.deleteBook(bookID2);
    }

    @Test
    public void getUserBooks() {

        Firebase fb = new Firebase();

        // test usernames for book owners
        String username1 = "testUsername1";
        String username2 = "testUsername2";

        // add borrowed book
        Book testBook1 = new Book();
        testBook1.setName("testBook1");
        testBook1.setISBN("1234567890");
        testBook1.setOwner(username1);
        String bookID1 = testBook1.getID();
        fb.addBook(testBook1);

        // add available book
        Book testBook2 = new Book(bookID2);
        testBook2.setName("testBook2");
        testBook2.setISBN("5432109876");
        testBook1.setOwner(username2);
        String bookID2 = testBook2.getID();
        fb.addBook(testBook2);

        // set expected result
        ArrayList<Book> listOfBooks = new ArrayList<>();
        listOfBooks.add(testBook2);

        ArrayList<Book> fbListOfBooks = fb.getUserBooks(username2);
        Assert.assertEquals(listOfBooks, fbListOfBooks);

        fb.deleteBook(bookID1);
        fb.deleteBook(bookID2);

    }

}
