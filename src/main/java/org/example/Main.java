package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final int NUM_ACCOUNTS = 4;
    public static final int NUM_TRANSACTIONS = 30;
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

        // Создаем и запускаем поток для выполнения транзакций
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new TransactionRunnable(accounts, random, NUM_TRANSACTIONS));

        // Ждем завершения всех транзакций
        executorService.shutdown();
    }
}