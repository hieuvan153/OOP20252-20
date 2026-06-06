package model.game_item;

import model.crop.*;

public class WaterRiceSeed extends Seed {
    public WaterRiceSeed() {
        super("Water Rice Seed", "water_rice", 10, 45, 5, 100, 10, 16);
    }

    @Override
    public Crop createCrop() {
        return new WaterRice();
    }
}
