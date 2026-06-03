package model.crop;

import model.soil.Cell;
import model.weather.Weather;
import utils.Constant;

/**
 * Baseline growth: needs moisture, nutrient, and sun present.
 * Scales by how close each is to the optimum.
 */
public class StandardGrowthManager implements GrowthManager {
    @Override
    public int calculateGrowthProgress(Cell cell, Weather weather) {
        double moistureFactor = Math.min(cell.getMoistureLevel() / 50.0, 1.0);
        double nutrientFactor = Math.min(cell.getNutrientLevel() / 50.0, 1.0);
        double sunlightFactor = Math.min(cell.getSunlightLevel() / 50.0, 1.0);
        double rate = (moistureFactor + nutrientFactor + sunlightFactor) / 3.0;
        return (int) Math.round(Constant.GROWTH_RATE * rate);
    }
}
