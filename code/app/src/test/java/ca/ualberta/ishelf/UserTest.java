package ca.ualberta.ishelf;
import org.junit.Test;
import org.junit.Assert;

import java.util.UUID;

import ca.ualberta.ishelf.Models.Rating;
import ca.ualberta.ishelf.Models.Request;
import ca.ualberta.ishelf.Models.User;

import static org.junit.Assert.*;
/**
 * changed tests to match altered user
 * @author Evan
 *
 */
public class UserTest {

    @Test
    public void addOwnedBookTest() {
        User Evan = new User();
        UUID fiftyShadesofGrey = new UUID(21341234L, 12341234L);
        Evan.addOwnedBook(fiftyShadesofGrey);
        Assert.assertTrue(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
        //assertTrue(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
    }
    @Test
    public void deleteOwnedBookTest() {
        User Evan = new User();
        UUID fiftyShadesofGrey = new UUID(123412L, 12341234L);
        Evan.deleteOwnedBook(fiftyShadesofGrey);
        assertFalse(Evan.getOwnedBooks().contains(fiftyShadesofGrey));
    }
    @Test
    public void getOverallRatingTest() {
        User Evan = new User();
        Rating utterGarbage = new Rating();
        utterGarbage.setRating(5);
        Evan.addRating(utterGarbage);
        Evan.getOverallRating();
        assertEquals(Evan.getOverallRating(), Evan.getRatingArrayList().get(0).getRating(), 0); //delta means difference
    }
    @Test
    public void addRequestTest(){
        User Evan = new User();
        Request request = new Request();
        request.setBookId(UUID.randomUUID());
        Evan.addRequest(request);
        assertEquals(Evan.getListofRequests().get(0).getBookId(), request.getBookId());
    }
    @Test
    public void deleteRequestTest(){
        User Evan = new User();
        Request request = new Request();
        request.setBookId(UUID.randomUUID());
        Evan.addRequest(request);
        Evan.deleteRequest(request);
        assertEquals(Evan.getListofRequests().size(), 0);
    }
}
