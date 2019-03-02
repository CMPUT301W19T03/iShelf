package ca.ualberta.ishelf;
import org.junit.Test;
import org.junit.Assert;

import java.util.UUID;

import static org.junit.Assert.*;
public class UserTest {

    @Test
    public void addBookTest() {
        User Evan = new User();
        Evan.setState("owner");
        UUID fiftyShadesofGrey = new UUID(21341234l, 12341234l);
        Evan.addBook(fiftyShadesofGrey);
        Assert.assertEquals(true, Evan.getOwnedBooks().contains(fiftyShadesofGrey));
        //assertTrue(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
    }
    @Test
    public void deleteBookTest() {
        User Evan = new User();
        Evan.setState("owner");
        UUID fiftyShadesofGrey = new UUID(123412l, 12341234l);
        Evan.deleteBook(fiftyShadesofGrey);
        assertFalse(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
    }
    @Test
    public void addRatingTest() {
        User Evan = new User();
        Evan.setState("borrower");
        Rating utterGarbage = new Rating();
        Evan.addRating(utterGarbage);
        assertTrue(Evan.getBorrowerRatings().contains(utterGarbage));
    }

}
