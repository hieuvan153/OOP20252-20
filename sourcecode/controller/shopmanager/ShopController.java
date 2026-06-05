package controller.shopmanager;

import controller.lifecyclemanager.BaseController;
import controller.lifecyclemanager.GameManager;
import model.crop.Crop;
import model.game_item.*;
import model.player_inventory.Player;
import model.soil.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShopController extends BaseController {
    private final List<ItemManager> catalog = new ArrayList<>();
    private Map<String, Integer> catalogPrices;
    private String lastMessage = "";
    private final Player player;

    public ShopController(Player player, GameManager gameManager, Cell[][] grid) {
        super(gameManager, grid);
        this.player = player;
        seedCatalog();
    }

    private void seedCatalog() {
        catalog.add(new SeedItem("Tomato Seed", TomatoSeed::new));
        catalog.add(new SeedItem("Sunflower Seed", SunflowerSeed::new));
        catalog.add(new SeedItem("Potato Seed", PotatoSeed::new));
        catalog.add(new SeedItem("Corn Seed", CornSeed::new));
        catalog.add(new SeedItem("Water Rice Seed", WaterRiceSeed::new));
        catalog.add(new FertilizerItem("Standard Fertilizer", StandardFertilizer::new));

        Map<String, Integer> prices =new LinkedHashMap<>();
        for (ItemManager im : catalog) {
            prices.put(im.getItem(), im.create().getPrice());
        }
        catalogPrices = Collections.unmodifiableMap(prices);
    }
    public List<ItemManager> getCatalog() {
        return Collections.unmodifiableList(catalog);
    }
    public String getLastMessage() {
        return lastMessage;
    }
    public Map<String, Integer> getCatalogPrices() {
        return catalogPrices;
    }

    public boolean buyItem(ItemManager entry, int quantity) {
        if (entry == null || quantity <= 0) {
            lastMessage = "Invalid purchase request.";
            return false;
        }

        Item prototype;
        try {
            prototype = entry.create();
        } catch (RuntimeException e) {
            lastMessage = "This item is currently unavailable.";
            return false;
        }
        if (prototype == null) {
            lastMessage = "This item is currently unavailable.";
            return false;
        }

        int totalCost;
        try {
            totalCost = Math.multiplyExact(prototype.getPrice(), quantity);
        } catch (ArithmeticException overflow) {
            lastMessage = "That quantity is too large to purchase.";
            return false;
        }

        if (!player.deductMoney(totalCost)) {
            lastMessage = "Not enough money for " + prototype.getName() + ".";
            return false;
        }

        Item canonical = player.getInventory().findByName(prototype.getName());
        if (canonical == null) {
            canonical = prototype;
        }
        player.getInventory().addItem(canonical, quantity);
        lastMessage = "Bought " + quantity + " x " + prototype.getName() + " for $" + totalCost + ".";
        return true;
    }

    public boolean sellCrop(Cell cell) {
        if (cell == null || cell.getCrop() == null) {
            lastMessage = "Nothing to sell here.";
            return false;
        }

        Crop crop = cell.getCrop();
        if (!crop.isHarvestable()) {
            lastMessage = "Crop is not ripe yet.";
            return false;
        }

        Crop harvested = cell.harvestCrop();
        int payout = harvested.getCropData().getValue();
        player.addMoney(payout);
        lastMessage = "Sold " + harvested.getName() + " for $" + payout + ".";
        return true;
    }

    @Override
    public void dayEnded() {

    }
}
