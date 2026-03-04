import Service.BankServices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        BankServices bank = new BankServices();
        bank.createAccount("A101", "rakesh", 1000);
        bank.createAccount("A102", "suresh", 1000);
        bank.createAccount("A103", "ramesh", 1000);

        try (ExecutorService service = Executors.newFixedThreadPool(10)) {

            for (int i = 0; i < 2000; i++) {
                service.submit(() -> bank.Deposit("A101", 10));
                service.submit(() -> bank.Withdraw("A101", 5));
                service.submit(() -> bank.transfer("A101", "A102", 20));
                service.submit(() -> bank.transfer("A102", "A103", 15));
                service.submit(() -> bank.Withdraw("A103", 10000));
            }

            service.shutdown();
            try {
                if (!service.awaitTermination(1, TimeUnit.MINUTES)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


            bank.displayFinalSummary();
            bank.printTransactionHistories("A101", "A102", "A101", "A102", "A103");
        }

    }
}
