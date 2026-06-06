package model.game_item;

import java.util.Objects;

public abstract class Item {
    protected final String name;
    protected final String assetKey;
    protected final int price;

    protected Item(String name, String assetKey, int price) {
        this.name = Objects.requireNonNull(name, "Item name must not be null");
        this.assetKey = Objects.requireNonNull(assetKey, "Asset key must not be null");
        if (price < 0) {
            throw new IllegalArgumentException("Item price must be >= 0, was " + price);
        }
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getAssetKey() {
        return assetKey;
    }

    public abstract String getStats();
}
