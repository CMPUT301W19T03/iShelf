package ca.ualberta.ishelf;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class Database {

    @Test
    public void testAddUser() {
        Database fb = new Database();
        User u1 = new User();
        u1.setUsername("testUsername");
        fb.addUser(u1);
        User fbUser= new User();
        fbUser = fb.getUser(u1.getUsername());
        Assert.assertEquals("testUsername", fbUser.getUsername());
        fb.deleteUser(u1.getUsername());
    }

    @Test
    public void testEditUser() {
        Database fb = new Database();
        // Add user
        User u1 = new User();
        u1.setUsername("testUsername");
        fb.addUser(u1);
        // Change user and edit
        User u2 = new User();
        u2.setUsername("testUsername");
        u2.setEmail("testContactInfo");
        fb.editUser(u2);
        u1 = fb.getUser("testUsername");
        fb.deleteUser(u1);
        Assert.assertEquals("testContactInfo", u1.getEmail());
    }

    @Test
    public void testDeleteUser() {

    }

    @Test
    public void testAddBook() {
        Database fb = new Database();
        Book b1 = new Book();
        b1.setName("testName");
        fb.addBook(b1);
        String bookID = b1.getID();
        Book b2 = newBook();
        b2 = fb.getBook(bookID);
        fb.deleteBook(b1.getId());
        UUID id = b1.getId;
        Assert.assertEquals(id, b2.getId());
    }

    @Test
    public void testEditBook() {
        Database fb = new Database();
        // Add book
        Book b1 = new Book();
        fb.addBook(b1);
        // Change book and edit
        Book b2 = new Book();
        u2.setName("TestName");
        u1.setUsername("testUsername");
        fb.addUser(u1);
        // Change book and edit
        u1.setName("AnotherTestName");
        fb.editBook(u1);
        u1 = fb.getBook(u1.getId());
        Assert.assertEquals("anotherTestName", u1.getId());
        fb.deleteUser(u1.getId());
    }

    @Test
    public void testDeleteBook() {
        FBInterface fb = new FBInterface(fburl);
        Book b1 = new Book();
        fb.addBook(b1);
        fb.deleteBook(b1.getID());
        //TODO what should we do if there is no matching book?
        Assert.assertEquals(null, fb.getBook(b1.getID()));
    }
}
