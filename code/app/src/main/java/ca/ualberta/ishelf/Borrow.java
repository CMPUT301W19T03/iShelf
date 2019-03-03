package ca.ualberta.ishelf;

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
