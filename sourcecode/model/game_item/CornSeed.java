package model.game_item;

import model.crop.*;

public class CornSeed extends Seed {
    public CornSeed(){
        super("Corn seed", 18, 75, 3, 75, 9, 10);
    }

    @Override
    public Crop createCrop() {
        return new Corn();
    }
}
