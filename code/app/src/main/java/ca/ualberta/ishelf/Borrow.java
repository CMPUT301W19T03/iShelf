package ca.ualberta.ishelf;

/**
 *THis is the borrow object which deals with the borrow transaction
 * @author : Mehrab
 */

public class Borrow {

    private User lender;



    private User borrower;


    public User getLender() {
        return lender;
    }


    public void setLender(User lender) {
        this.lender = lender;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }




}
