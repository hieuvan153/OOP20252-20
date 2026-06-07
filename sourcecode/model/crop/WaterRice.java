package model.crop;

public class WaterRice extends Crop {
    public WaterRice() {
        super(new CropData(20, 75, 75, 15),
                new StandardStateManager(4, 2),
                new FloodLovingStressManager(),
                new FloodLovingGrowthManager());
    }

    @Override
    public String getName() {
        return "Water Rice";
    }
}
