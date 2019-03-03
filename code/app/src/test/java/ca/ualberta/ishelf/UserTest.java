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
        UUID fiftyShadesofGrey = new UUID(21341234L, 12341234L);
        Evan.addBook(fiftyShadesofGrey);
        Assert.assertTrue(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
        //assertTrue(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
    }
    @Test
    public void deleteBookTest() {
        User Evan = new User();
        Evan.setState("owner");
        UUID fiftyShadesofGrey = new UUID(123412L, 12341234L);
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
