package ca.ualberta.ishelf;

import org.junit.Assert;
import org.junit.Test;

public class BookTest {

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

    @Test
    public void addRating(){
        // TODO: implement the logic for this test
    }

}