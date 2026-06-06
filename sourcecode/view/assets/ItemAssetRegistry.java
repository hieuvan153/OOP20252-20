package view.assets;

import model.crop.*;
import model.game_item.*;

import java.util.HashMap;
import java.util.Map;

public final class ItemAssetRegistry {
    private static final Map<Class<?>, String> KEYS = new HashMap<>();

    static {
        KEYS.put(TomatoSeed.class, "tomato");
        KEYS.put(PotatoSeed.class, "potato");
        KEYS.put(CornSeed.class, "corn");
        KEYS.put(SunflowerSeed.class, "sunflower");
        KEYS.put(WaterRiceSeed.class, "water_rice");

        KEYS.put(StandardFertilizer.class, "standard_fertilizer");
    }

    public static String getKey(Item item) {
        if (item == null) {
            return null;
        }

        return KEYS.get(item.getClass());
    }
}