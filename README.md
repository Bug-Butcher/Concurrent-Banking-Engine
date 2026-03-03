# 💳 Concurrent Banking System (Java)

A multi-threaded banking engine built in Java to simulate real-world financial transactions under heavy concurrent load.

This project focuses on **thread safety, data consistency, and stress testing** rather than UI.

---

  <p align="center">
  <img src="https://img.shields.io/badge/Java-21-blue" />
  <img src="https://img.shields.io/badge/Concurrency-Thread%20Safe-success" />
  <img src="https://img.shields.io/badge/Stress%20Test-10k%2B%20Ops-brightgreen" />
  <img src="https://img.shields.io/badge/Architecture-Layered%20Design-blueviolet" />
</p>


## 🚀 Features

- ✅ Thread-safe Deposit, Withdrawal, and Transfer operations
- ✅ ExecutorService-based concurrent processing
- ✅ 10,000+ operations stress tested
- ✅ Random latency simulation (real-world behavior)
- ✅ Prevents negative balances
- ✅ Handles insufficient balance gracefully
- ✅ Transaction history tracking per account
- ✅ Sorted transaction logs by transaction ID
- ✅ Final bank balance consistency validation

---

## 🏗 Tech Stack

- Java
- ExecutorService
- ThreadLocalRandom
- Custom Exception Handling
- Synchronized Blocks (Concurrency Control)

---

## 🧠 Concurrency Design

- All balance-modifying operations are synchronized
- Transfer operations are atomic
- Failed transactions do not affect system state
- Random delays simulate real-world network latency
- No race conditions observed under heavy load

---

## 🔥 Stress Test Configuration

- Thread Pool: 5–10 threads
- Operations: 10,000+
- Random delays: 1–9ms per operation

Each iteration performs:
- Deposit
- Withdrawal
- Transfer between accounts
- Failed withdrawal simulation

---

## 📊 Sample Output (Final Summary)
```
======= FINAL SUMMARY =======

Account: A101 | Holder: rakesh | Balance: ₹10.00
Account: A102 | Holder: suresh | Balance: ₹10.00
Account: A103 | Holder: ramesh | Balance: ₹4710.00
```



✔ No negative balances  
✔ No corrupted data  
✔ No deadlocks  
✔ Total bank balance remains consistent

---

## 📈 What This Project Demonstrates

- Strong understanding of multi-threading
- Handling race conditions
- Maintaining financial invariants
- Atomic transaction handling
- Concurrent system testing
- Backend-focused engineering mindset

---

## 📂 Project Structure
```
├── Main.java
├── Service/
│   └── BankServices.java
├── Model/
│   ├── Account.java
│   ├── Transaction.java
│   ├── TransactionType.java
│   └── TransactionStatus.java
├── Exception/
│   ├── InvalidAmountException.java
│   ├── InsufficientBalanceException.java
│   ├── DuplicateAccountException.java
│   └── AccountNotFoundException.java
├── Task/
│   └── UserTask.java

```


## 🏗 Architecture Overview

The system follows a layered, service-oriented structure:

```mermaid
    A[Main] --> B[ExecutorService Thread Pool]
    B --> C[UserTask (Runnable)]
    C --> D[BankServices]

    D --> E[Account Model]
    D --> F[Transaction Model]

    D --> G[TransactionType Enum]
    D --> H[TransactionStatus Enum]

    D --> I[Custom Exceptions]
    I --> I1[InvalidAmountException]
    I --> I2[InsufficientBalanceException]
    I --> I3[DuplicateAccountException]
    I --> I4[AccountNotFoundException]
```

### 🔎 Flow Explanation

1. **Main** initializes accounts and thread pool.
2. **ExecutorService** runs concurrent banking operations.
3. **UserTask** encapsulates transaction logic.
4. **BankServices** handles all business logic.
5. **Models & Enums** maintain structured transaction data.
6. **Custom Exceptions** ensure safe and controlled failure handling.

This architecture ensures:
- Clear separation of concerns
- Thread-safe transaction handling
- Maintainable and extensible backend design
---

## 🧪 How To Run

1. Clone the repository
2. Open in IntelliJ / Eclipse
3. Run `Main.java`
4. Observe concurrent logs and final summary

---

## 🎯 Future Improvements

- Convert to Spring Boot REST API(not-confirmed)
- Replace synchronized with ReentrantLock
- Add performance benchmarking (ops/sec)

- Add REST-level concurrent API testing(not-confirmed)

---

## 👨‍💻 Author

Shubh Jaiswal  
Computer Science Student | Backend & Java Enthusiast

---

If you found this project interesting, feel free to connect or provide feedback!
