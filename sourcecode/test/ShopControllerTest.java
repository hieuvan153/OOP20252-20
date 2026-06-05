package test;

import controller.lifecyclemanager.GameManager;
import controller.shopmanager.ShopController;
import model.crop.*;
import model.player_inventory.Player;
import model.soil.Cell;
import model.soil.FarmMap;

public class ShopControllerTest {
    private static void check(boolean pass, String msg) {
        if (!pass) {
            throw new AssertionError(msg);
        }
    }

    private static ShopController shop(Player p) {
        GameManager gm = GameManager.getInstance();
        gm.reset();
        return new ShopController(p, gm, new FarmMap().getGrid());
    }

    private static Cell cropCell(boolean ripe) {
        Crop crop = new Crop(new CropData(10, 50, 50), new StandardStateManager(4, 3), new StandardStressManager(), new StandardGrowthManager());
        Cell c = new Cell(0,0);
        c.till();
        c.plantCrop(crop);

        if (ripe) {
            while (!crop.isHarvestable() && !crop.isDead()) {
                c.updateSunlightAmount(100);
                c.updateNutrientAmount(100);
                c.updateMoistureAmount(100);
                crop.dailyUpdate(c, null);
            }
        }
        return c;
    }

    public static void run() {
        ShopController s1 = shop(new Player());
        check(s1.getCatalog().size() == 6 && s1.getCatalogPrices().size() == 6, "catalog and price map should have 6 entries");
        check(s1.getCatalogPrices().get("Tomato Seed") == 15, "Tomato Seed price should be 15");

        Player p2 = new Player();
        ShopController s2 = shop(p2);
        check(s2.buyItem(s2.getCatalog().get(0), 2), "buying 2 Tomato Seed should succeed");
        check(p2.getMoney() == 170 && p2.getInventory().getItemCount(p2.getInventory().findByName("Tomato Seed")) == 2, "money and inventory updated successfully");

        Player p3 = new Player();
        ShopController s3 = shop(p3);
        check(!s3.buyItem(s3.getCatalog().get(0), 1000) && p3.getMoney() == 200, "buying beyond funds failed");
        check(s3.getLastMessage().toLowerCase().contains("not enough"), "message: not enough");

        ShopController s4 = shop(new Player());
        check(!s4.buyItem(null, 1) && !s4.buyItem(s4.getCatalog().get(0), 0), "null quantity rejected");

        ShopController s5 = shop(new Player());
        check(!s5.sellCrop(cropCell(false)) && s5.getLastMessage().toLowerCase().contains("not ripe"), "selling unripe failed");

        Player p6 = new Player();
        ShopController s6 = shop(p6);
        Cell ripe = cropCell(true);
        int before = p6.getMoney();
        check(s6.sellCrop(ripe) && p6.getMoney() > before && ripe.getCrop() == null, "sell successfully");
        check(!shop(new Player()).sellCrop(new Cell(0, 0)), "selling from empty cell failed");
    }

    public static void main(String[] args) {
        run();
        System.out.println("PASSED: ShopControllerTest");
    }
}
