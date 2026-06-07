package test;

import controller.lifecyclemanager.GameManager;
import controller.shopmanager.ItemManager;
import controller.shopmanager.ShopController;
import model.crop.Crop;
import model.game_item.Item;
import model.game_item.Seed;
import model.game_item.TomatoSeed;
import model.player_inventory.Player;
import model.soil.Cell;
import model.soil.FarmMap;
import model.weather.Weather;
import model.weather.WeatherType;

public class ShopControllerTest {
    private static void check(boolean pass, String msg) {
        if (!pass) {
            throw new AssertionError(msg);
        }
    }

    private static ShopController fresh(Player player, FarmMap map) {
        GameManager gm = GameManager.getInstance();
        gm.reset();
        return new ShopController(player, gm, map.getGrid());
    }

    private static ItemManager firstSeed(ShopController shop) {
        for (ItemManager im : shop.getCatalog()) {
            if (im.create() instanceof Seed) return im;
        }
        throw new AssertionError("catalog has no seed entry");
    }

    // Plant a tomato on the cell and grow it under good conditions until harvestable.
    private static Crop ripenTomato(Cell cell) {
        Crop crop = new TomatoSeed().createCrop();
        cell.till();
        cell.plantCrop(crop);
        for (int i = 0; i < 40 && !crop.isHarvestable() && !crop.isDead(); i++) {
            cell.updateMoistureAmount(100);
            cell.updateNutrientAmount(100);
            cell.updateSunlightAmount(100);
            crop.dailyUpdate(cell, Weather.of(WeatherType.SUNNY));
        }
        return crop;
    }

    public static void run() {
        ShopController shop = fresh(new Player(), new FarmMap());
        check(!shop.getCatalog().isEmpty(), "catalog should not be empty");
        check(!shop.getCatalogPrices().isEmpty(), "catalog prices should be published");

        // buyItem
        Player buyer = new Player();
        ShopController shop2 = fresh(buyer, new FarmMap());
        ItemManager seedEntry = firstSeed(shop2);
        int price = shop2.getCatalogPrices().get(seedEntry.getItem());
        int before = buyer.getMoney();

        check(shop2.buyItem(seedEntry, 1), "buying an affordable seed succeeds");
        check(buyer.getMoney() == before - price, "money is reduced by the price");
        Item bought = buyer.getInventory().findByName(seedEntry.getItem());
        check(bought != null && buyer.getInventory().getItemCount(bought) == 1,
                "the bought seed lands in the inventory");
        check(shop2.getLastMessage().startsWith("Bought"), "a success message is recorded");

        // buyItem: invalid requests
        check(!shop2.buyItem(null, 1), "null entry is rejected");
        check(!shop2.buyItem(seedEntry, 0), "non-positive quantity is rejected");

        // buyItem: not enough money
        Player broke = new Player();
        ShopController shop3 = fresh(broke, new FarmMap());
        broke.deductMoney(broke.getMoney()); // -> 0
        check(!shop3.buyItem(firstSeed(shop3), 1), "cannot buy without money");
        check(shop3.getLastMessage().contains("Not enough money"), "an insufficient-funds message is recorded");

        // sellCrop: negative cases
        Player seller = new Player();
        FarmMap map = new FarmMap();
        ShopController shop4 = fresh(seller, map);
        check(!shop4.sellCrop(null), "selling a null cell fails");
        check(!shop4.sellCrop(map.getCell(0, 0)), "selling an empty cell fails");

        Cell unripe = map.getCell(0, 1);
        unripe.till();
        unripe.plantCrop(new TomatoSeed().createCrop());
        check(!shop4.sellCrop(unripe), "selling an unripe crop fails");

        // sellCrop
        Cell ripe = map.getCell(1, 0);
        Crop crop = ripenTomato(ripe);
        check(crop.isHarvestable(), "test setup: tomato should ripen under good conditions");
        int moneyBefore = seller.getMoney();
        int value = crop.getCropData().getValue();
        check(shop4.sellCrop(ripe), "selling a ripe crop succeeds");
        check(seller.getMoney() == moneyBefore + value, "selling pays the crop value");
        check(ripe.getCrop() == null, "the cell is empty after selling");
    }

    public static void main(String[] args) {
        run();
        System.out.println("PASSED: ShopControllerTest");
    }
}
