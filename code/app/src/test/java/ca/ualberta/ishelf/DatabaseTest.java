package ca.ualberta.ishelf;

import org.junit.Assert;
import org.junit.Test;

public class Database {

    @Test
    public void testAddUser() {
        Database fb = new Database();
        User u1 = new User();
        u1.setUsername("testUsername");
        fb.addUser(u1);
        User fbUser= new User();
        User fbUser = fb.getUser(u1.getUsername());
        Assert.assertEquals(u1, fbUser);
        fb.deleteUser(u1.getUsername());
    }

    @Test
    public void testEditUser() {
        Database fb = new Database(fburl);
        User u1 = new User();
        // TODO implement this properly
        fb.addUser(u1);
        User u2 = new User();
        String id = "123";
        //TODO is this how edit works?
        u2 = fb.editUser(id);
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
        Assert.assertEquals(b1, b2);
    }

    @Test
    public void testEditBook() {
        Database fb = new Database();
        // Add user
        User u1 = new User();
        u1.setUsername("testUsername");
        fb.addUser(u1);
        // Change user and edit
        User u2 = new User();
        u2.setUsername("testUsername");
        Book b1 = new Book();
        u2.addBook(b1);
        fb.editUser(u2);


        u2 = fb.editUser(u1);
        u2 = fb.editUser(u1.getUsername());
        fb.getUser(id);
    }

    @Test
    public void testDeleteBook() {
        FBInterface fb = new FBInterface(fburl);
        Book b1 = new Book();
        fb.addBook(b1);
        fb.deleteBook(b1.getID());
        //TODO what should we do if there is no matching book?
        Assert.assertEquals(fb.getBook(b1.getID(), null));
    }
}

// firebase tracks notifications, books, users,
// add, get by id, update
// TODO what is the name of our class, i don't think it can be FBInterface
// TODO what do the edit commands actually do?
// TODO what are the arguments for Delete commands, id? or object?
// TODO how is a firebase instance initialized, does it take a URL?

}
