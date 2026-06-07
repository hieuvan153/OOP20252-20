package controller.lifecyclemanager;

import model.soil.Cell;

import java.util.Objects;

public abstract class BaseController {
    protected final GameManager gameManager;
    protected final Cell[][] grid;

    protected BaseController(GameManager gameManager, Cell[][] grid) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        this.grid = Objects.requireNonNull(grid, "grid must not be null");
    }
}
