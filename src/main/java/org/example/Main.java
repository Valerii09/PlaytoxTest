package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final int NUM_ACCOUNTS = 4;
    public static final int NUM_TRANSACTIONS = 30;
    public static final int NUM_THREADS = 4;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        Random random = new Random();

        // Создаем счета
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            String id = UUID.randomUUID().toString();
            int money = INITIAL_BALANCE;
            accounts.add(new Account(id, money));
        }

        // Вычисляем количество транзакций на каждый поток
        int transactionsPerThread = NUM_TRANSACTIONS / NUM_THREADS;
        int remainingTransactions = NUM_TRANSACTIONS % NUM_THREADS;

        // Создаем и запускаем потоки для выполнения транзакций
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < NUM_THREADS; i++) {
            int transactions = transactionsPerThread;
            if (i < remainingTransactions) {
                transactions++; // Добавляем оставшиеся транзакции к первым потокам
            }
            executorService.submit(new TransactionRunnable(accounts, random, transactions));
        }

        // Ждем завершения всех транзакций
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}