package model.soil;

import model.crop.Crop;

public class Beetle implements Pest {
    private static final int DAMAGE = 15;

    /** How many simulated days this beetle has been on the field. */
    private int daysAlive = 0;

    @Override
    public void attackTargetCrop(Crop crop) {
        if (crop == null) return;
        if (daysAlive == 0) {
            daysAlive++;          // grace day — just spawned, no stress yet
            return;
        }
        daysAlive++;
        crop.addStress(DAMAGE);
    }

    @Override
    public String getName() {
        return "Beetle";
    }
}
