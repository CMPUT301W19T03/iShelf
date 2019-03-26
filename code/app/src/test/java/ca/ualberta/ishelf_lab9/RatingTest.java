package ca.ualberta.ishelf_lab9;

import org.junit.Assert;
import org.junit.Test;

/**
 * rating unit test
 * @author Mehrab
 * @Edited Evan
 * rating unit test
 */
public class RatingTest {
    @Test
    public void setRatingtest(){
        Rating rate = new Rating();
        rate.setRating((float)4);

        float rate1=4;

        Assert.assertEquals(rate1,rate.getRating(),0.0002);

    }
    @Test
    public void getRatingtest(){
        Rating rate = new Rating();
        rate.setRating((float)15);

        float rate1=-1;

        Assert.assertEquals(rate1,rate.getRating(), 0.0002);

    }


    @Test
    public void setCommenttest(){
        Rating rate = new Rating();
        rate.setComment("nice");

        String rate1="nice";

        Assert.assertEquals(rate1,rate.getComment());
    }
    @Test
    public void getCommenttest(){
        Rating rate = new Rating();
        rate.setComment("good");

        String rate1="good";

        Assert.assertEquals(rate1,rate.getComment());

    }




}
