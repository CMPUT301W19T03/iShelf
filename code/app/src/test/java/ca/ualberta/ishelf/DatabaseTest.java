package ca.ualberta.ishelf;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

public class DatabaseTest {
//    @Test
//    public void testAddUser() {
//        //TODO get this working
//        Database fb = new Database(this);
//        User u1 = new User();
//        u1.setUsername("testUsername");
//        fb.addUser(u1);
//        User fbUser= new User();
//        fbUser = fb.getUser(u1.getUsername());
//        Assert.assertEquals("testUsername", fbUser.getUsername());
//        fb.deleteUser(u1.getUsername());
//    }
//
//    @Test
//    public void testEditUser() {
//        Database fb = new Database(this);
//        // Add user
//        User u1 = new User();
//        u1.setUsername("testUsername");
//        fb.addUser(u1);
//        // Change user and edit
//        User u2 = new User();
//        u2.setUsername("testUsername");
//        u2.setEmail("testContactInfo");
//        fb.editUser(u2);
//        u1 = fb.getUser("testUsername");
//        fb.deleteUser(u1.getUsername());
//        Assert.assertEquals("testContactInfo", u1.getEmail());
//    }
//
//    @Test
//    public void testDeleteUser() {
//
//    }
//
//    @Test
//    public void testAddBook() {
//        Database fb = new Database(this);
//        Book b1 = new Book();
//        b1.setName("testName");
//        fb.addBook(b1);
//        UUID bookID = b1.getId();
//        Book b2 = new Book();
//        b2 = fb.getBook(bookID);
//        fb.deleteBook(b1.getId());
//        UUID id = b1.getId();
//        Assert.assertEquals(id, b2.getId());
//    }
//
//    @Test
//    public void testEditBook() {
//        Database fb = new Database(this);
//        // Add book
//        Book b1 = new Book();
//        fb.addBook(b1);
//        // Change book and edit
//        Book b2 = new Book();
//        b2.setName("TestName");
//        b1.setDescription("testDescription");
//        fb.addBook(b1);
//        // Change book and edit
//        b1.setName("AnotherDescription");
//        fb.editBook(b1.getId());
//        b1 = fb.getBook(b1.getId());
//        Assert.assertEquals("AnotherDescription", b1.getDescription());
//        fb.deleteBook(b1.getId());
//    }
//
//    @Test
//    public void testDeleteBook() {
//        Database fb = new Database(this);
//        Book b1 = new Book();
//        fb.addBook(b1);
//        fb.deleteBook(b1.getId());
//        //TODO what should we do if there is no matching book?
//        Assert.assertEquals(null, fb.getBook(b1.getId()));
//    }
//
//    @Test
//    public void getUser() {
//        Database fb = new Database(this);
//
//        // add a user
//        String username = "testUsername";
//        User testUser = new User();
//        testUser.setUsername(username);
//        testUser.setEmail("test@test.ca");
//        fb.addUser(testUser);
//
//        User fbUser = fb.getUser(username);
//        Assert.assertEquals(testUser, fbUser);
//
//        fb.deleteUser(username);
//    }
//
//    @Test
//    public void getBook() {
//        Database fb = new Database(this);
//
//        // add a book
//        Book testBook = new Book();
//        testBook.setName("testBook");
//        testBook.setISBN(1234567890l);
//        UUID bookID = testBook.getId();
//        fb.addBook(testBook);
//
//        Book fbBook = fb.getBook(bookID);
//        Assert.assertEquals(testBook, fbBook);
//
//        fb.deleteBook(bookID);
//    }
//
//    @Test
//    public void getBooks() {
//        Database fb = new Database(this);
//
//        // add the first book
//        Book testBook1 = new Book();
//        testBook1.setName("testBook1");
//        testBook1.setISBN(1234567890l);
//        UUID bookID1 = testBook1.getId();
//        fb.addBook(testBook1);
//
//        // add a second book
//        Book testBook2 = new Book();
//        testBook2.setName("testBook2");
//        testBook2.setISBN(5432109876l);
//        UUID bookID2 = testBook2.getId();
//        fb.addBook(testBook2);
//
//        // set expected result
//        ArrayList<Book> listOfBooks = new ArrayList<>();
//        listOfBooks.add(testBook1);
//        listOfBooks.add(testBook2);
//
//        ArrayList<Book> fbListOfBooks = fb.getBooks();
//        Assert.assertEquals(listOfBooks, fbListOfBooks);
//
//        fb.deleteBook(bookID1);
//        fb.deleteBook(bookID2);
//    }
//
//    @Test
//    public void getAvailableBooks() {
//        Database fb = new Database(this);
//
//        // add borrowed book
//        Book testBook1 = new Book();
//        testBook1.setName("testBook1");
//        testBook1.setISBN(1234567890l);
//        testBook1.setBorrowed();
//        UUID bookID1 = testBook1.getId();
//        fb.addBook(testBook1);
//
//        // add available book
//        Book testBook2 = new Book();
//        testBook2.setName("testBook2");
//        testBook2.setISBN(5432109876l);
//        testBook2.setAvailable();
//        UUID bookID2 = testBook2.getId();
//        fb.addBook(testBook2);
//
//        // set expected result
//        ArrayList<Book> listOfBooks = new ArrayList<>();
//        listOfBooks.add(testBook2);
//
//        ArrayList<Book> fbListOfBooks = fb.getAvailableBooks();
//        Assert.assertEquals(listOfBooks, fbListOfBooks);
//
//        fb.deleteBook(bookID1);
//        fb.deleteBook(bookID2);
//    }
//
//    @Test
//    public void getUserBooks() {
//
//        Database fb = new Database(this);
//
//        // test usernames for book owners
//        String username1 = "testUsername1";
//        String username2 = "testUsername2";
//
//        // add borrowed book
//        Book testBook1 = new Book();
//        testBook1.setName("testBook1");
//        testBook1.setISBN(1234567890l);
//        testBook1.setOwner(username1);
//        UUID bookID1 = testBook1.getId();
//        fb.addBook(testBook1);
//
//        // add available book
//        //TODO why is this bookID "2"
//        //Book testBook2 = new Book(bookID1);
//        Book testBook2 = new Book();
//        testBook2.setId(bookID1);
//        testBook2.setName("testBook2");
//        testBook2.setISBN(5432109876l);
//        testBook1.setOwner(username2);
//        UUID bookID2 = testBook2.getId();
//        fb.addBook(testBook2);
//
//        // set expected result
//        ArrayList<Book> listOfBooks = new ArrayList<>();
//        listOfBooks.add(testBook2);
//
//        ArrayList<Book> fbListOfBooks = fb.getUserBooks(username2);
//        Assert.assertEquals(listOfBooks, fbListOfBooks);
//
//        fb.deleteBook(bookID1);
//        fb.deleteBook(bookID2);
//
//    }

}
