package ca.ualberta.ishelf;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.ualberta.ishelf.Models.Request;

/**
 * request unit test
 * @author Evan
 * @edited Evan
 *
 */
public class RequestTest {

    @Test
    public void setRequesttest(){

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date testDate = null;
        String date = "02/30/2012";
        try{
            testDate = df.parse(date);
        } catch (ParseException e){ System.out.println("invalid format");}

        if (!df.format(testDate).equals(date)){
            System.out.println("invalid date!!");
        } else {
            System.out.println("valid date");
        }
        Request req = new Request();
        req.setTimeRequested(testDate);

        

        Assert.assertEquals(testDate,req.getTimeRequested());

    }
}
