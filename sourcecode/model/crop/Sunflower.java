package model.crop;

public class Sunflower extends Crop {
    public Sunflower() {
        super("sunflower",
                new CropData(10, 35, 70),
                new StandardStateManager(4, 4),
                new DroughtResistanceStressManager(),
                new SunLovingGrowthManager());
    }
    @Override
    public String getName() {
        return "Sunflower";
    }
}
