package ca.ualberta.ishelf;
import org.junit.Test;

import static org.junit.Assert.*;
public class UserClassTest {
    @Test
    private void deleteBookTest{
        User Evan = new User();
        Book fiftyShadesofGrey = new Book;
        Evan.deleteBook(fiftyShadesofGrey);
        assertFalse(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
    }
    @Test
    private void addRatingTest{
        User Evan = new User();
        Evan.setState("borrower");
        Rating utterGarbage = new Rating;
        Evan.addRating(utterGarbage);
        assertTrue(Evan.getBorrowerRatings().contains(utterGarbage));
    }
}
