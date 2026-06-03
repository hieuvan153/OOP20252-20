package model.crop;

import model.soil.Cell;
import model.weather.Weather;
import model.weather.WeatherType;
import utils.Constant;

/**
 * Growth strategy for water-loving plants (e.g. Water Rice):
 * moisture dominates; rain accelerates growth.
 */
public class FloodLovingGrowthManager implements GrowthManager {
    @Override
    public int calculateGrowthProgress(Cell cell, Weather weather) {
        double moisture = Math.min(cell.getMoistureLevel() / 70.0, 1.0);
        double nutrient = Math.min(cell.getNutrientLevel() / 40.0, 1.0);
        double sun      = Math.min(cell.getSunlightLevel() / 50.0, 1.0);
        double rate = (moisture * 0.6) + (nutrient * 0.2) + (sun * 0.2);
        if (weather != null && weather.getType() == WeatherType.RAINY) rate *= 1.3;
        return (int) Math.round(Constant.GROWTH_RATE * rate);
    }
}
