package model.player_inventory;

import utils.Constant;

public class Player {
    private int money;
    private final Inventory inventory;

    public Player() {
        this.money = Constant.START_MONEY;
        this.inventory = new Inventory();
    }

    public int getMoney() {
        return money;
    }

    public Inventory getInventory() {
        return inventory;
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
}
