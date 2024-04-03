package org.example;

class Account {
    private final String id;
    private int money;

    public Account(String id, int money) {
        this.id = id;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public synchronized int getMoney() {
        return money;
    }

    public synchronized void deposit(int amount) {
        money += amount;
    }

    public synchronized void withdraw(int amount) {
        if (money >= amount) {
            money -= amount;
        } else {
            // Если недостаточно средств, возможно, нужно сгенерировать исключение или вернуть какой-то код ошибки
            throw new IllegalArgumentException("Not enough money in account " + id + " to complete transaction or invalid amount");
        }
    }
}
