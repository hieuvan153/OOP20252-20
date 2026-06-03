package model.crop;

import model.soil.Cell;
import model.weather.Weather;
import model.weather.WeatherType;
import utils.Constant;

/**
 * Growth strategy for sun-loving plants (e.g. Sunflower, Tomato):
 * sunlight is much more important than moisture and nutrient
 */
public class SunLovingGrowthManager implements GrowthManager {
    @Override
    public int calculateGrowthProgress(Cell cell, Weather weather) {
        double sun = Math.min(cell.getSunlightLevel() / 70.0, 1.0);
        double moisture = Math.min(cell.getMoistureLevel() / 50.0, 1.0);
        double nutrient = Math.min(cell.getNutrientLevel() / 40.0, 1.0);
        double rate = (sun * 0.6) + (moisture * 0.2) + (nutrient * 0.2);
        if (weather != null && weather.getType() == WeatherType.SUNNY) rate *= 1.25;
        return (int) Math.round(Constant.GROWTH_RATE * rate);
    }

}
