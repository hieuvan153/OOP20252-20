package model.crop;

public class Tomato extends Crop {
    public Tomato() {
        super("tomato",
                new CropData(15, 60, 80),
                new StandardStateManager(4, 3),
                new StandardStressManager(),
                new SunLovingGrowthManager());
    }

    @Override
    public String getName() {
        return "Tomato";
    }
}
