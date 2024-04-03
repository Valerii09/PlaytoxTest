package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

class TransactionRunnable implements Runnable {
    private final List<Account> accounts;
    private final Random random;
    private final int numTransactions;
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
                logger.error("Thread interrupted while sleeping", e);
                Thread.currentThread().interrupt(); // Preserve interrupt status
            }

            // Выполняем транзакцию
            Account from = getRandomAccount();
            Account to;
            do {
                to = getRandomAccount();
            } while (from.getId().equals(to.getId())); // Проверка, чтобы аккаунты не были одинаковыми

            int amount = random.nextInt(from.getMoney() + 1);

            // Блокируем сначала меньший номер аккаунта, затем больший, чтобы избежать дэдлока
            Account firstAccount = (from.getId().compareTo(to.getId()) < 0) ? from : to;
            Account secondAccount = (from.getId().compareTo(to.getId()) < 0) ? to : from;

            synchronized (firstAccount) {
                synchronized (secondAccount) {
                    from.withdraw(amount);
                    to.deposit(amount);
                    String message = "Transaction: " + from.getId() + " -> " + to.getId() + ", Amount: " + amount;
                    logger.info(message);
                    System.out.println(message); // Выводим в консоль для наглядности
                }
            }
        }
    }

    private Account getRandomAccount() {
        return accounts.get(random.nextInt(accounts.size()));
    }
}
