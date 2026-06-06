package view.assets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class Assets {
    // TOOL ICONS
    public static Image HOE_ICON;
    public static Image WATER_ICON;
    public static Image SEED_ICON;
    public static Image HAND_ICON;
    public static Image FERTILIZER_ICON;

    // WEATHER ICONS
    public static Image SUN_ICON;
    public static Image RAIN_ICON;
    public static Image DRY_ICON;

    // TILES
    public static Image DIRT_TILE;
    public static Image GRASS_TILE;

    // SHOP ITEM ICONS
    private static final Map<String, Image> SHOP_ICONS = new HashMap<>();

    // LOAD ALL ASSETS
    public static void load() {
        System.out.println("Loading assets...");

        // TOOLS
        HOE_ICON = loadImage("/view/assets/tools/hoe.png");
        WATER_ICON = loadImage("/view/assets/tools/watering_can.png");
        SEED_ICON = loadImage("/view/assets/tools/seed.png");
        HAND_ICON = loadImage("/view/assets/tools/hand.png");
        FERTILIZER_ICON = loadImage("/view/assets/tools/fertilizer.png");

        // WEATHER
        SUN_ICON = loadImage("/view/assets/weather/sun.png");
        RAIN_ICON = loadImage("/view/assets/weather/rain.png");
        DRY_ICON = loadImage("/view/assets/weather/dry.png");

        // SHOP
        loadShopIcon("corn");
        loadShopIcon("standard_fertilizer");
        loadShopIcon("potato");
        loadShopIcon("sunflower");
        loadShopIcon("tomato");
        loadShopIcon("water_rice");

        // TILES
        /*
        DIRT_TILE = loadImage("/view/assets/tiles/dirt.png");
        GRASS_TILE = loadImage("/view/assets/tiles/grass.png");
        */

        System.out.println("Assets loaded successfully.");
    }

    private static void loadShopIcon(String name) {
        SHOP_ICONS.put(name.toLowerCase(), loadImage("/view/assets/shop/" + name + ".png"));
    }

    public static Image getShopIcon(String itemName) {
        return SHOP_ICONS.get(itemName.toLowerCase());
    }

    // LOAD IMAGE
    private static Image loadImage(String path) {
        return new Image(Assets.class.getResourceAsStream(path));
    }

    // CREATE IMAGE VIEW
    public static ImageView imageView(Image image, double size) {
        ImageView view = new ImageView(image);

        view.setFitWidth(size);
        view.setFitHeight(size);
        view.setPreserveRatio(true);
        view.setSmooth(false);

        return view;
    }
}