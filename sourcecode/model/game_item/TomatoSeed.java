package model.game_item;

import model.crop.*;

public class TomatoSeed extends Seed {
    public TomatoSeed() {
        super("Tomato Seed", "tomato", 15, 60, 3, 80, 6, 10);
    }

    @Override
    public Crop createCrop() {
        return new Tomato();
    }
}
