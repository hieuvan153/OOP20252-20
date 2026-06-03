package model.crop;

public class Corn extends Crop {
    public Corn() {
        super(new CropData(12, 45, 85), new StandardStateManager(4, 3), new StandardStressManager(), new StandardGrowthManager());
    }

    @Override
    public String getName() {
        return "Corn";
    }
}
