package Service;

import Model.Account;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import Exception.insufficientBalanceException;
import Exception.AccountNotFoundException;
import Exception.DuplicateAccountException;
import Exception.InvalidAmountException;
import Model.Transaction;
import Model.TransactionStatus;
import Model.TransactionType;

public class BankServices {
    Map<String, Account> accounts = new HashMap<>();
    private final AtomicLong transactionCounter = new AtomicLong(1);


//    FOR CREATING A NEW ACCOUNT

    public void createAccount(String accountNumber,String name,double initialBalance){
        if (accountNumber == null || accountNumber.isBlank()){
            throw new IllegalArgumentException("Account number cannot be empty");
        }
        if (accounts.containsKey(accountNumber)){
            throw new DuplicateAccountException("Account already exists");
        }
        if (initialBalance < 0){
            throw new insufficientBalanceException("Starting balance cannot be negative");
        }

        Account account = new Account(accountNumber,name,initialBalance);
        accounts.put(accountNumber,account);
        System.out.println("Account created Successfully for "+name);
    }



//    FOR ACCESSING INFORMATION ABOUT A ACCOUNT

    public Account getAccount(String accountNumber){
        Account account = accounts.get(accountNumber);
        if (account == null){
            throw new AccountNotFoundException("Invalid account number");
        }
        return account;
    }


//    RETURNS THE TOTAL MONEY THE BANK HOLDS AT A POINT

    public double getTotalBankBalance(){
        return accounts.values()
                .stream()
                .mapToDouble(Account::getBalance)
                .sum();

    }




//    DEPOSITING INTO ACCOUNT

    public void Deposit(String AccNo,double amount){
      //  simulateDelay();
        if (amount <= 0){
            throw new InvalidAmountException("Cannot deposit negative or zero amount");
        }
        Account account = getAccount(AccNo);
        long transactionId = transactionCounter.getAndIncrement();
        synchronized (account) {
             try{
                account.deposit(amount);
                Transaction transaction = new Transaction(transactionId,
                        TransactionType.DEPOSIT,
                        amount,
                        AccNo,
                        AccNo);
                 account.addTransaction(transaction);
                System.out.printf(
                        "[%s] SUCCESS | DEPOSIT | ₹%.2f | %s | Balance: ₹%.2f%n",
                        Thread.currentThread().getName(),
                        amount,
                        AccNo,
                        account.getBalance()
                );
            }catch (AccountNotFoundException | insufficientBalanceException |
                    InvalidAmountException e){
                 Transaction transaction = new Transaction(transactionId,
                         TransactionType.DEPOSIT,
                         amount,
                         AccNo,
                         AccNo,
                         e.getMessage());
                 account.addTransaction(transaction);
                 System.out.printf(
                         "[%s] FAILED  | DEPOSIT | ₹%.2f | %s | Reason: %s%n",
                         Thread.currentThread().getName(),
                         amount,
                         AccNo,
                         e.getMessage()
                 );
             }
        }

    }



//    WITHDRAWING FROM A ACCOUNT

    public void Withdraw(String AccNo,double amount){
      //  simulateDelay();
        Account account = getAccount(AccNo);
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid Amount,Enter valid Amount");
        }else if (amount > account.getBalance()) {
            throw new insufficientBalanceException(
                    "Insufficient balance in account " + AccNo +
                            ". Current balance: ₹" + String.format("%.2f", account.getBalance())
            );
        }
        long transactionId = transactionCounter.getAndIncrement();
        synchronized (account){
            try {
                account.withdraw(amount);
                Transaction transaction = new Transaction(transactionId,
                        TransactionType.WITHDRAWAL,
                        amount,
                        AccNo,
                        AccNo);
                account.addTransaction(transaction);
                System.out.printf(
                        "[%s] SUCCESS | WITHDRAW | ₹%.2f | %s | Balance: ₹%.2f%n",
                        Thread.currentThread().getName(),
                        amount,
                        AccNo,
                        account.getBalance()
                );
            }
            catch (AccountNotFoundException | insufficientBalanceException |
                    InvalidAmountException e){
                Transaction transaction = new Transaction(transactionId,
                        TransactionType.WITHDRAWAL,
                        amount,
                        AccNo,
                        AccNo,
                        e.getMessage());
                account.addTransaction(transaction);
                System.out.printf(
                        "[%s] FAILED  | DEPOSIT | ₹%.2f | %s | Reason: %s%n",
                        Thread.currentThread().getName(),
                        amount,
                        AccNo,
                        e.getMessage()
                );
            }
        }
    }



//TRANSFERRING AMOUNT BETWEEN MULTIPLE ACCOUNTS

    public void transfer(String fromAccNo,String  toAccNo,double amount){
      //  simulateDelay();
        if (amount <= 0){
            throw new InvalidAmountException("Cannot transfer negative or zero amount");
        }
        if (fromAccNo.equals( toAccNo)){
            throw new DuplicateAccountException("Both accounts cannot be same!");
        }
        Account fromAccount = getAccount(fromAccNo);
        Account  toAccount = getAccount(toAccNo);

        Account firstLock = fromAccNo.compareTo(toAccNo) < 0 ? fromAccount : toAccount;
        Account secondLock = fromAccNo.compareTo(toAccNo)< 0 ? toAccount : fromAccount;

        long transactionId = transactionCounter.getAndIncrement();
        synchronized (firstLock) {
            synchronized (secondLock) {

                try {
                    fromAccount.withdraw(amount);
                    toAccount.deposit(amount);
                    Transaction transaction = new Transaction(transactionId,
                            TransactionType.TRANSFER,
                            amount,
                            fromAccNo,
                            toAccNo);

                    fromAccount.addTransaction(transaction);
                    toAccount.addTransaction(transaction);

                    System.out.println(
                            "[" + Thread.currentThread().getName() + "] " +
                                    "SUCCESS | TRANSFER | ₹" + String.format("%.2f", amount) +
                                    " | " + fromAccNo + " → " + toAccNo +
                                    " | Balances: " +
                                    fromAccNo + "=₹" + String.format("%.2f",
                                    fromAccount.getBalance()) +
                                    " , " +
                                    toAccNo + "=₹" + String.format("%.2f", toAccount.getBalance())
                    );

                } catch (AccountNotFoundException | insufficientBalanceException |
                         InvalidAmountException | DuplicateAccountException e) {
                    Transaction transaction = new Transaction(transactionId,
                            TransactionType.TRANSFER,
                            amount,
                            fromAccNo,
                            toAccNo,
                            e.getMessage());
                    fromAccount.addTransaction(transaction);
                    System.out.println(
                            "[" + Thread.currentThread().getName() + "] " +
                                    "FAILURE | TRANSFER | ₹" + String.format("%.2f", amount) +
                                    " | " + fromAccNo + " → " + toAccNo +
                                    " | Reason: Insufficient Balance" +
                                    " | Available: ₹" +
                                    String.format("%.2f", fromAccount.getBalance())
                    );
                }
            }
        }
    }

    public void displayFinalSummary(){
        System.out.println("\n=======  FINAL SUMMARY  =======");
        accounts.values()
                .stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .forEach(account -> System.out.printf(
                        "Account: %s | Holder: %s | Balance: ₹%.2f%n",
                        account.getAccountNumber(),
                        account.getAccountHolderName(),
                        account.getBalance()
                ));

        System.out.printf("-----------------------------------------%n");
        System.out.printf("TOTAL BANK BALANCE: ₹%.2f%n", getTotalBankBalance());
        System.out.println("=========================================");
    }



    public void printTransactionHistories(String ...accountNumber){
        Set<String> AccountNumber = new HashSet<>(Arrays.asList(accountNumber));
        for (String s : AccountNumber) {
            Account account = getAccount(s);
            System.out.printf("""
                    ==============================PRINTING TRANSACTION HISTORY\
                     OF ACC. NO.\
                     %s==============================
                    """,account.getAccountNumber());
            System.out.println("\n========== TRANSACTION HISTORY ==========");
        System.out.println("Account Number: " + s);
        System.out.println("Account Holder: " + account.getAccountHolderName());

        if (account.getTransactionList().isEmpty()) {
            System.out.println("No transactions found for this account.");
            System.out.println("=========================================");
            return;
        }

        account.getTransactionList().stream()
                .sorted(Comparator.comparingLong(Transaction::getTransactionId).reversed())
                .forEach(transaction ->{
                System.out.printf(
                        "--------------------------------------------------%n" +
                                "Transaction ID: %d%n" +
                                "Type: %s%n" +
                                "Status: %s%n"+
                                "From: %s%n" +
                                "To: %s%n" +
                                "Amount: ₹%.2f%n",
                        transaction.getTransactionId(),
                        transaction.getType(),
                        transaction.getStatus(),
                        transaction.getFromAccNo(),
                        transaction.getToAccNo(),
                        transaction.getAmount()
                );
            if (transaction.getStatus() == TransactionStatus.FAILURE) {
                System.out.printf("Reason: %s%n", transaction.getFailureReason());
            }

            System.out.printf(
                    "Time: %s%n" +
                            "--------------------------------------------------%n",
                    transaction.getTimeStamp()
            );
        }
        );
        System.out.println("=========================================");//  }

        }
    }








//    SIMULATING DELAY FOR STRESS TEST
private void simulateDelay() {
    try {
        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 10));
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
}



