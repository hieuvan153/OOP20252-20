package controller.lifecyclemanager;

import model.soil.Cell;

import java.util.Objects;

public abstract class BaseController implements DayObserver {
    protected final GameManager gameManager;
    protected final Cell[][] grid;

    protected BaseController(GameManager gameManager, Cell[][] grid) {
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        this.grid = Objects.requireNonNull(grid, "grid must not be null");
        gameManager.addObserver(this);
    }

    @Override
    public abstract void dayEnded();
}
