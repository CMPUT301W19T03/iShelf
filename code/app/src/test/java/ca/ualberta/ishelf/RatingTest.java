package ca.ualberta.ishelf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class RatingTest {
    @Test
    public void setRatingtest(){
        Rating rate = new Rating();
        rate.setRating(4);

        int rate1=4;

        Assert.assertEquals(rate1,rate.getRating());

    }
    @Test
    public void getRatingtest(){
        Rating rate = new Rating();
        rate.setRating(4);

        int rate1=4;

        Assert.assertEquals(rate1,rate.getRating());

    }



}
