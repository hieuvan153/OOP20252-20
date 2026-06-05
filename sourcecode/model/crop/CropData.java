package model.crop;

/**
 * Immutable attributes of species (cost to buy as seed,
 * sell value at harvest, and max stress threshold before death).
 */
public class CropData {
    private final int cost;
    private final int value;
    private final int maxStress;

    public CropData(int cost, int value, int maxStress) {
        // maxStress is a divisor in the growth formula and the death threshold;
        // 0 (or negative) would mean "dies instantly"
        if (maxStress <= 0) {
            throw new IllegalArgumentException("maxStress must be > 0 (got " + maxStress + ")");
        }

        this.cost = cost;
        this.value = value;
        this.maxStress = maxStress;
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
}
