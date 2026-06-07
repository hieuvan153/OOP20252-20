package model.player_inventory;

import utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private int money;
    private final Inventory inventory = new Inventory();
    private final List<MoneyObserver> moneyObservers = new ArrayList<>();

    public Player() {
        this.money = Constant.START_MONEY;
    }

    public int getMoney() {
        return money;
    }

    public Inventory getInventory() {
        return inventory;
    }

    // Register a listener invoked whenever the balance changes (and not duplicated).
    public void addMoneyObserver(MoneyObserver observer) {
        Objects.requireNonNull(observer, "observer must not be null");
        if (!moneyObservers.contains(observer)) {
            moneyObservers.add(observer);
        }
    }

    public void addMoney(int amount) {
        if (amount <= 0) return;

        try {
            money = Math.addExact(money, amount);
        } catch (ArithmeticException overflow) {
            money = Integer.MAX_VALUE;
        }
    }

    public boolean deductMoney(int amount) {
        if (amount <= 0 || money < amount) return false;
        money -= amount;
        return true;
    }

    private void notifyMoneyChanged() {
        for (MoneyObserver observer : moneyObservers) {
            observer.onMoneyChanged(money);
        }
    }
}