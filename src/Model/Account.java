package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Exception.InvalidAmountException;
import Exception.insufficientBalanceException;

public class Account {
    private final String accountNumber;
    private final String accountHolderName;
    private double balance;
    private final List<Transaction> transcationList= new ArrayList<>();


//    CONSTRUCTOR
    public Account(String accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }


//ALL THE GETTERS
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionList() {
        return Collections.unmodifiableList(transcationList);
    }
//    GETTERS END HERE


//DEPOSIT METHOD
public synchronized void  deposit(double amount){
        if (amount <= 0){
            throw new InvalidAmountException("Invalid Amount,Enter valid Amount");
        }
        balance += amount;
    }

//WITHDRAWAL METHOD
public synchronized void withdraw(double amount){
        if (amount <= 0){
            throw new InvalidAmountException("Invalid Amount,Enter valid Amount");
        } else if (amount > balance) {
            throw new insufficientBalanceException(
                    "Insufficient balance in account " + accountNumber +
                            ". Current balance: ₹" + String.format("%.2f", balance)
            );
        }
        balance -= amount;
    }


    public synchronized void addTransaction(Transaction transaction){
        transcationList.add(transaction);
    }


}
