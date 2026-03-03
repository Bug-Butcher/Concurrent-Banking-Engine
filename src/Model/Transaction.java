package Model;

import java.time.LocalDateTime;

public class Transaction {
    private final long transactionId;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timeStamp;
    private final String  fromAccNo;
    private final String toAccNo;
    private final TransactionStatus status;
    private final String failureReason;




// SUCCESS TRANSACTION CONSTRUCTOR
public Transaction(long transactionId,
                   TransactionType type,
                   double amount,
                   String fromAccNo,
                   String toAccNo) {

    this.transactionId = transactionId;
    this.type = type;
    this.amount = amount;
    this.fromAccNo = fromAccNo;
    this.toAccNo = toAccNo;
    this.status = TransactionStatus.SUCCESS;
    this.failureReason = null;
    this.timeStamp = LocalDateTime.now();
}




    // FAILED TRANSACTION CONSTRUCTOR
    public Transaction(long transactionId,
                       TransactionType type,
                       double amount,
                       String fromAccNo,
                       String toAccNo,
                       String failureReason) {

        if (failureReason == null || failureReason.isBlank()) {
            throw new IllegalArgumentException("Failure reason cannot be null or empty");
        }

        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.fromAccNo = fromAccNo;
        this.toAccNo = toAccNo;
        this.status = TransactionStatus.FAILURE;
        this.failureReason = failureReason;
        this.timeStamp = LocalDateTime.now();
    }


    public long getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getFromAccNo() {
        return fromAccNo;
    }

    public String getToAccNo() {
        return toAccNo;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
