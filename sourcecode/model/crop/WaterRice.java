package model.crop;

public class WaterRice extends Crop {
    public WaterRice() {
        super("water_rice",
                new CropData(20, 75, 75),
                new StandardStateManager(4, 2),
                new FloodLovingStressManager(),
                new FloodLovingGrowthManager());
    }

    @Override
    public String getName() {
        return "Water Rice";
    }
}
