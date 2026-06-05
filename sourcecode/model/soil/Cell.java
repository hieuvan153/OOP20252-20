package model.soil;

import model.crop.Crop;
import model.weather.Weather;
import utils.Constant;

import java.util.Objects;

public class Cell {
    private final int x;
    private final int y;

    private int nutrientLevel;
    private int moistureLevel;
    private int sunlightLevel;

    private CellState state;
    private Crop currentCrop;
    private Pest currentPest;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.nutrientLevel = Constant.DEFAULT_NUTRIENT;
        this.moistureLevel = 40;
        this.sunlightLevel = 60;
        this.state = CellState.UNTILLED;
        this.currentCrop = null;
        this.currentPest = null;
    }

    // ---------- Getters ----------
    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }

    public int getNutrientLevel(){
        return nutrientLevel;
    }

    public int getMoistureLevel(){
        return moistureLevel;
    }

    public int getSunlightLevel(){
        return sunlightLevel;
    }

    public CellState getCellState() {
        return state;
    }

    public Crop getCrop(){
        return currentCrop;
    }

    public Pest getPest(){
        return currentPest;
    }

    // ---------- Player / system actions ----------
    /** hoe the cell */
    public boolean till() {
        if (state == CellState.UNTILLED) {
            state = CellState.TILLED;
            return true;
        }
        return false;
    }

    /** plant crop on cell */
    public boolean plantCrop(Crop crop) {
        Objects.requireNonNull(crop, "crop must not be null when planting");
        if (state != CellState.TILLED || currentCrop != null) return false;   // the cell must be tilled and doesn't have crop on it
        this.currentCrop = crop;
        return true;
    }

    /** Harvest the crop if it is in HARVEST state */
    public Crop harvestCrop() {
        if (currentCrop != null && currentCrop.isHarvestable()) {
            Crop harvested = currentCrop;
            currentCrop = null;
            state = CellState.UNTILLED;     // soil exhausted; needs re-tilling
            return harvested;
        }
        return null;
    }

    /** Spawn pest on a cell */
    public void spawnPest(Pest pest) {
        this.currentPest = Objects.requireNonNull(pest, "pest must not be null");
    }

    /** Remove pest from the cell */
    public void handlePest() {
        this.currentPest = null;
    }

    public void clearCrop() {
        this.currentCrop = null;
    }

    public void changeState(CellState newState) {
        this.state = newState;
    }

    // ------ Resource updates (called by weather + tools) -----------

    public void updateNutrientAmount(int delta) {
        nutrientLevel = safeCheckingValue(nutrientLevel + delta, 0, Constant.MAX_NUTRIENT);
    }

    public void updateMoistureAmount(int delta) {
        moistureLevel = safeCheckingValue(moistureLevel + delta, 0, Constant.MAX_MOISTURE);
    }

    public void updateSunlightAmount(int delta) {
        sunlightLevel = safeCheckingValue(sunlightLevel + delta, 0, Constant.MAX_SUNLIGHT);
    }

    /**
     * Daily simulation step for the cell:
     *   1) weather applies its effect to soil stats
     *   2) a crop (if any) grows / suffers stress and drinks/eats from the soil;
     *      empty soil instead slowly recovers nutrient toward the default
     *   3) the pest (if any) attacks the crop
     *   4) baseline moisture evaporation, independent of crops
     */
    public void dailyUpdate(Weather weather) {
        if (weather != null) weather.applyWeatherEffect(this);

        if (currentCrop != null) {
            currentCrop.dailyUpdate(this, weather);
        }
        else if (nutrientLevel < Constant.DEFAULT_NUTRIENT) {
            // empty soil regenerates nutrient back up to the default
            int regen = Math.min(Constant.NUTRIENT_REGEN_RATE, Constant.DEFAULT_NUTRIENT - nutrientLevel);
            updateNutrientAmount(regen);
        }
        if (currentPest != null && currentCrop != null && !currentCrop.isDead()) {
            currentPest.attackTargetCrop(currentCrop);
        }

        updateMoistureAmount(-Constant.MOISTURE_EVAPORATION);
    }

    private int safeCheckingValue(int v, int low, int high) {
        if(v > high) return high;
        if(v < low) return low;
        return v;
    }
}
