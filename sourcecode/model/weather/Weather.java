package model.weather;

import model.soil.Cell;
import java.util.Objects;

public class Weather {
    private final WeatherType type;
    private int remainingDuration;
    private final WeatherEffect effect;

    public Weather(WeatherType type, int duration, WeatherEffect effect) {
        this.type = Objects.requireNonNull(type, "weather type must not be null");
        this.remainingDuration = duration;
        this.effect = Objects.requireNonNull(effect, "weather effect must not be null");
    }

    public WeatherType getType() {
        return type;
    }

    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    public void applyWeatherEffect(Cell cell) {
        if (cell == null) return;
        effect.applyEffect(cell);
    }

    public void tickDuration() {
        if (remainingDuration > 0) remainingDuration--;
    }

    // return a weather object of a WeatherType
    public static Weather of(WeatherType type) {
        switch (type) {
            case RAINY:
                return new Weather(type, 1, new RainyEffect());
            case DROUGHT:
                return new Weather(type, 1, new DroughtEffect());
            case SUNNY:
            default:
                return new Weather(type, 1, new SunnyEffect());
        }
    }
}
