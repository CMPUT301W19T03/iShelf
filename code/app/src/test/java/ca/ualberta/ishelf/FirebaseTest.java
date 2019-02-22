package ca.ualberta.ishelf;
import org.junit.Test;
import ca.ualberta.ishelf.FBInterface;
import static org.junit.Assert.assertEquals;
public class FBInterfaceTest {
    String fburl;

    @Test
    public void testAddUser() {
        FBInterface fb = new FBInterface(fburl);
        User u1 = new User();
        fb.addUser(u1);
    }

    @Test
    public void testEditUser() {
        FBInterface fb = new FBInterface(fburl);
        User u1 = new User();
        // TODO implement this properly
        fb.addUser(u1)
        User u2 = new User();
        String id = "123";
        u2 = fb.editUser(id);
    }

/*
@Test
public void testDeleteUser() {

}
*/

    @Test
    public void testAddBook() {
        FBInterface fb = new FBInterface(fburl);
        Book b1 = new Book();
        fb.addBook(b1);
    }

    @Test
    public void testEditBook() {
        FBInterface fb = new FBInterface(fburl);
        User u1 = new User();
        // TODO implement this properly
        fb.addUser(u1)
        User u2 = new User();
        u2 = fb.editUser(id);
        String id = "123";
        fb.getUser(id)
    }

    @Test
    public void testDeleteBook() {
        FBInterface fb = new FBInterface(fburl);
        Book b1 = new Book();
        fb.addBook(b1);
        fb.deleteBook(b1);
    }
}

// firebase tracks notifications, books, users,
// add, get by id, update
// TODO what is the name of our class, i don't think it can be FBInterface
// TODO what do the edit commands actually do?
// TODO what are the arguments for Delete commands, id? or object?
// TODO how is a firebase instance initialized, does it take a URL?

}
