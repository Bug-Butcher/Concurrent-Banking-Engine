package Task;

import Service.BankServices;

public class UserTask implements Runnable{

    private final BankServices bankservice;
    private final String fromAccount;
    private final String toAccount;
    private final double amount;

    public UserTask(BankServices bankservice, String fromAccount, String toAccount, double amount) {
        this.bankservice = bankservice;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            bankservice.transfer(fromAccount,toAccount,amount);
        }
        catch (Exception e){
            System.out.println("Transaction failed :"+e.getMessage());
        }
    }
}
