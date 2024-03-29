package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class TransactionRunnable implements Runnable {
    private List<Account> accounts;
    private Random random;
    private int numTransactions;
    private static final Logger logger = LogManager.getLogger(TransactionRunnable.class);

    public TransactionRunnable(List<Account> accounts, Random random, int numTransactions) {
        this.accounts = accounts;
        this.random = random;
        this.numTransactions = numTransactions;
    }

    @Override
    public void run() {
        for (int i = 0; i < numTransactions; i++) {
            try {
                Thread.sleep(random.nextInt(1001) + 1000); // Засыпаем от 1000 до 2000 мс
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Выполняем транзакцию
            Account from = getRandomAccount();
            Account to = getRandomAccount();
            int amount = random.nextInt(from.getMoney() + 1);
            transferMoney(from, to, amount);
        }
    }

    private Account getRandomAccount() {
        return accounts.get(random.nextInt(accounts.size()));
    }

    private void transferMoney(Account from, Account to, int amount) {
        synchronized (from) {
            synchronized (to) {
                if (from.getMoney() >= amount) {
                    from.setMoney(from.getMoney() - amount);
                    to.setMoney(to.getMoney() + amount);
                    String message = "Transaction: " + from.getId() + " -> " + to.getId() + ", Amount: " + amount;
                    logger.info(message);
                    System.out.println(message); // Выводим в консоль для наглядности
                }
            }
        }
    }
}