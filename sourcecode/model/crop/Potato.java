package model.crop;

public class Potato extends Crop {
    public Potato() {
        super(new CropData(8, 25, 90, 12),
                new StandardStateManager(4, 4),
                new DroughtResistanceStressManager(),
                new StandardGrowthManager());
    }

    @Override
    public String getName() {
        return "Potato";
    }
}
