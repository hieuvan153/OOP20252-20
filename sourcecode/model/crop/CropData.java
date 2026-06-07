package model.crop;

public class CropData {
    private final int cost;
    private final int value;
    private final int maxStress;
    private final int waterPerDay;  // moisture ammount that the crop consumes from its cell per day

    public CropData(int cost, int value, int maxStress, int waterPerDay) {
        if (maxStress <= 0) {
            throw new IllegalArgumentException("maxStress must be > 0 (got " + maxStress + ")");
        }
        if (waterPerDay < 0) {
            throw new IllegalArgumentException("waterPerDay must be >= 0 (got " + waterPerDay + ")");
        }

        this.cost = cost;
        this.value = value;
        this.maxStress = maxStress;
        this.waterPerDay = waterPerDay;
    }

    public int getCost(){
        return cost;
    }

    public int getValue(){
        return value;
    }

    public int getMaxStress() {
        return maxStress;
    }

    public int getWaterPerDay() {
        return waterPerDay;
    }
}
