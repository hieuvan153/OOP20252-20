package model.soil;

import model.weather.Weather;
import utils.Constant;

public class FarmMap {
    private final Cell[][] grid;
    private final int width;
    private final int height;

    public FarmMap(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("FarmMap dimensions must be > 0 (width=" + width + ", height=" + height + ")");
        }

        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c] = new Cell(c, r);
            }
        }
    }

    public FarmMap() {
        this(Constant.DEFAULT_FARM_COLS, Constant.DEFAULT_FARM_ROWS);
    }

    // some getters used in controller
    public Cell[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) return null;
        return grid[row][col];
    }

    /** Apply the daily tick to every cell. Used by controller */
    public void updateAllCells(int day, Weather weather) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c].dailyUpdate(weather);
            }
        }
    }
}
