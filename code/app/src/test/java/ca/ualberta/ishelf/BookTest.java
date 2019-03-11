package ca.ualberta.ishelf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Evan
 * book unit test
 */
public class BookTest {

    // Tests for the Getters and Setters
    @Test
    public void testName(){
        Book book = new Book();
        book.setName("Book Name");
        Assert.assertEquals("Book Name", book.getName());
    }

    @Test
    public void testDescription(){
        Book book = new Book();
        book.setDescription("Description of books");
        Assert.assertEquals("Description of books", book.getDescription());
    }

    @Test
    public void testISBN(){
        Book book = new Book();
        Long ISBN = new Long(1234567890);
        book.setISBN(ISBN);
        Assert.assertEquals(ISBN, book.getISBN());
    }

    @Test
    public void testStatus(){
        Book book = new Book();
        Assert.assertEquals(1, book.getStatus());
        book.setStatus(0);
        Assert.assertEquals(0, book.getStatus());
    }

    @Test
    public void testId(){
        Book book = new Book();
        UUID id = UUID.randomUUID();
        book.setId(id);
        Assert.assertEquals(id, book.getId());
    }

    @Test
    public void testRatings(){
        Book book = new Book();
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setComment("Rating Comment");
        book.addRating(rating);
        ArrayList<Rating> ratingArrayList = book.getRatings();
        Assert.assertEquals(rating, ratingArrayList.get(ratingArrayList.size()-1));
    }


    // Tests for non-getter/setter methods
    @Test
    public void setBorrowed(){
        Book book = new Book();
        Assert.assertEquals(1, book.getStatus()); // default initialization is Available (1)

        book.setBorrowed();
        Assert.assertEquals(0, book.getStatus());
    }

    @Test
    public void setAvailable(){
        Book book = new Book();
        book.setBorrowed();
        book.setAvailable();
        Assert.assertEquals(1, book.getStatus());
    }

    @Test
    public void checkBorrowed(){
        Book book = new Book();
        Assert.assertFalse(book.checkBorrowed());

        book.setBorrowed();
        Assert.assertTrue(book.checkBorrowed());
    }
}