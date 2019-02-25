package ca.ualberta.ishelf;
import org.junit.Test;

import java.util.UUID;
import static org.junit.Assert.*;
public class UserClassTest {
    @Test
    public void addBookTest(){
        User Evan = new User();
        Evan.setState("owner");
        Book book = new Book();
        Evan.addBook(book.getId());
        assertTrue(Evan.getOwnedBooks().contains(book.getId()));
    }
    @Test
    public void deleteBookTest(){
        User Evan = new User();
        Evan.setState("owner");
        UUID fiftyShadesofGrey = UUID.fromString("fiftyShadesofGrey");
        Evan.deleteBook(fiftyShadesofGrey);
        assertFalse(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
    }
    @Test
    public void addRatingTest(){
        User Evan = new User();
        Evan.setState("borrower");
        Rating utterGarbage = new Rating();
        utterGarbage.setRating(4);
        utterGarbage.setComment("hello");
        Evan.addRating(utterGarbage);
        assertTrue(Evan.getBorrowerRatings().contains(utterGarbage));
    }

}
