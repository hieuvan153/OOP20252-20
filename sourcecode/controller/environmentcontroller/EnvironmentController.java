package controller.environmentcontroller;

import controller.lifecyclemanager.BaseController;
import controller.lifecyclemanager.GameManager;
import model.soil.Beetle;
import model.soil.Cell;
import model.soil.FarmMap;
import model.weather.Weather;
import model.weather.WeatherType;
import utils.Constant;

import java.text.CompactNumberFormat;
import java.util.List;
import java.util.Random;

public class EnvironmentController extends BaseController {
    private final FarmMap farmMap;
    private final Random rng = new Random();

    public EnvironmentController(GameManager gameManager, FarmMap farmMap) {
        super(gameManager, farmMap.getGrid());
        this.farmMap = farmMap;
    }

    public Weather generateWeather() {
        int roll = rng.nextInt(100);
        WeatherType chosen;

        if (roll < Constant.DROUGHT_CHANCE) {
            chosen = WeatherType.DROUGHT;
        } else if (roll < Constant.DROUGHT_CHANCE + Constant.RAIN_CHANCE) {
            chosen = WeatherType.RAINY;
        } else {
            chosen = WeatherType.SUNNY;
        }

        Weather w = Weather.of(chosen);
        gameManager.setCurrentWeather(w);
        return w;
    }

    public void spawnPest() {
        int roll = rng.nextInt(100);
        List<Cell> candidates = farmMap.getRandomCellsNumber_of(farmMap.getHeight() * farmMap.getWidth());
        for (Cell c : candidates) {
            if (c.getCrop() != null && c.getPest() == null) {
                c.spawnPest(new Beetle());
                return;
            }
        }
    }

    @Override
    public void dayEnded() {
        generateWeather();
        spawnPest();
    }
}
