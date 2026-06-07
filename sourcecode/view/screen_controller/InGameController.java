package view.screen_controller;

import controller.actionmanager.FertilizerBag;
import controller.actionmanager.Hand;
import controller.actionmanager.Hoe;
import controller.actionmanager.PlayerController;
import controller.actionmanager.SeedBag;
import controller.actionmanager.ToolStrategy;
import controller.actionmanager.WateringCan;
import controller.democontroller.DemoController;
import controller.lifecyclemanager.GameManager;
import exception.SmartFarmException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.crop.Crop;
import model.crop.GrowthState;
import model.game_item.Item;
import model.player_inventory.Player;
import model.soil.Cell;
import model.soil.CellState;
import model.soil.FarmMap;
import model.weather.WeatherType;
import view.assets.Assets;
import view.screen_util.SceneRouter;
import view.screen_util.Screens;

import java.io.IOException;
import java.util.function.Consumer;


public class InGameController {

    // --- tool hotbar ---
    @FXML private ToggleButton hoeTool;
    @FXML private ToggleButton waterTool;
    @FXML private ToggleButton seedTool;
    @FXML private ToggleButton handTool;
    @FXML private ToggleButton fertilizerTool;

    // --- weather selector ---
    @FXML private ToggleButton sunWeatherButton;
    @FXML private ToggleButton rainWeatherButton;
    @FXML private ToggleButton dryWeatherButton;

    // --- layout + labels ---
    @FXML private BorderPane rootPane;
    @FXML private GridPane farmGrid;
    @FXML private Label toolLabel;
    @FXML private TextField notificationField;
    @FXML private TextField dayField;
    @FXML private Label moneyField;
    @FXML private Label cellLabel;
    @FXML private Label waterLabel;
    @FXML private Label nutrientLabel;
    @FXML private Label cropLabel;

    private SceneRouter router;

    // injected domain
    private FarmMap farmMap;
    private Player player;
    private GameManager gameManager;
    private PlayerController playerController;
    private DemoController demoController;

    // tool strategy instances
    private Hoe hoe;
    private WateringCan wateringCan;
    private SeedBag seedBag;
    private Hand hand;
    private FertilizerBag fertilizerBag;

    private Tile[][] tiles;
    private Cell hovered;

    // ======================== FXML lifecycle ========================

    @FXML
    private void initialize() {
        rootPane.getStylesheets().add(getClass().getResource("/view/css/Style.css").toExternalForm());
        setupToolIcons();
        setupWeatherIcons();
    }

    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    // Inject the live session and wire all gameplay. Called once after load.
    public void setGame(FarmMap farmMap, Player player, GameManager gameManager,
                        PlayerController playerController, DemoController demoController) {
        this.farmMap          = farmMap;
        this.player           = player;
        this.gameManager      = gameManager;
        this.playerController = playerController;
        this.demoController   = demoController;

        this.hoe            = new Hoe();
        this.wateringCan    = new WateringCan();
        this.seedBag        = new SeedBag(player);
        this.hand           = new Hand(player);
        this.fertilizerBag  = new FertilizerBag("Standard Fertilizer", player);

        moneyField.setText("$" + player.getMoney());
        player.addMoneyObserver(amount -> moneyField.setText("$" + amount));
        wireButtons();
        setKeyHandlers();
        buildGrid();
        selectTool(hand, handTool);
        Platform.runLater(this::refreshAll);
    }

    // ======================== wiring ========================

    private void wireButtons() {
        hoeTool.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { selectTool(hoe, hoeTool); }
        });
        waterTool.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { selectTool(wateringCan, waterTool); }
        });
        seedTool.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { openSeedPicker(); }
        });
        handTool.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { selectTool(hand, handTool); }
        });
        fertilizerTool.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { selectTool(fertilizerBag, fertilizerTool); }
        });

        sunWeatherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { forceWeather(WeatherType.SUNNY); }
        });
        rainWeatherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { forceWeather(WeatherType.RAINY); }
        });
        dryWeatherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { forceWeather(WeatherType.DROUGHT); }
        });
    }

    private void setKeyHandlers() {
        rootPane.setFocusTraversable(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() { rootPane.requestFocus(); }
        });

        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ESCAPE:  togglePause();   break;
                    case DIGIT1:  openSeedPicker(); break;
                    case DIGIT2:  selectTool(hoe, hoeTool);            break;
                    case DIGIT3:  selectTool(wateringCan, waterTool);  break;
                    case DIGIT4:  selectTool(hand, handTool);          break;
                    case DIGIT5:  selectTool(fertilizerBag, fertilizerTool); break;
                    case S:       forceWeather(WeatherType.SUNNY);     break;
                    case R:       forceWeather(WeatherType.RAINY);     break;
                    case D:       forceWeather(WeatherType.DROUGHT);   break;
                    case F:
                        demoController.fastForwardGrowthMap();
                        InGameController.this.notify("Fast-forwarded crops");
                        refreshAll();
                        break;
                    case P:
                        boolean any = demoController.spawnDemoPest();
                        InGameController.this.notify(any ? "Spawned a pest" : "No crops to infest");
                        refreshAll();
                        break;
                    case T:
                        demoController.tillAll();
                        InGameController.this.notify("Tilled every cell");
                        refreshAll();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    // ======================== actions ========================

    @FXML
    private void openShop() {
        router.show(Screens.SHOP);
    }

    @FXML
    private void onNextDay() {
        gameManager.advanceDay();
        notify("Day " + gameManager.getCurrentDay() + " — " + weatherName());
        refreshAll();
    }

    private void selectTool(ToolStrategy tool, ToggleButton btn) {
        playerController.useTool(tool);
        if (btn != null) btn.setSelected(true);
        // switching away from the seed bag restores its default icon
        if (tool != seedBag) {
            resetSeedToolIcon();
        }
        ToolStrategy cur = playerController.getCurrentTool();
        toolLabel.setText("TOOL : " + (cur != null ? cur.getName().toUpperCase() : "(none)"));
    }

    private void forceWeather(WeatherType type) {
        demoController.forceWeather(type);
        notify("Weather forced: " + type);
        refreshAll();
    }

    private void onCellClick(Cell cell) {
        if (router.hasOverlay()) return;
        boolean ok = playerController.interactWithCell(cell);
        notify(ok ? "" : "Can't use this tool here");
        revertSeedToolIfEmpty();
        refreshAll();
        updateHover(cell);
    }

    // If the seed bag is active and its loaded seed is used up, revert to the default icon.
    private void revertSeedToolIfEmpty() {
        if (playerController.getCurrentTool() != seedBag) return;
        String name = seedBag.getSeedName();
        Item item = (name != null) ? player.getInventory().findByName(name) : null;
        if (item == null || player.getInventory().getItemCount(item) <= 0) {
            resetSeedToolIcon();
        }
    }

    private void togglePause() {
        if (router.hasOverlay()) {
            router.closeOverlay();
        } else {
            router.showOverlay(router.getScreen(Screens.PAUSE));
        }
    }

    private void openSeedPicker() {
        if (router.hasOverlay()) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Screens.SEED_PICKER_FXML));
            Parent overlay = loader.load();
            SeedPickerController picker = loader.getController();

            picker.init(player,
                    new Consumer<String>() {
                        @Override
                        public void accept(String name) {
                            seedBag.loadSeed(name);
                            selectTool(seedBag, seedTool);
                            updateSeedToolIcon(name);
                            InGameController.this.notify("Loaded " + name);
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            router.closeOverlay();
                        }
                    }
            );
            router.showOverlay(overlay);
        } catch (IOException ex) {
            throw new SmartFarmException("Could not open the seed picker", ex);
        }
    }

    // ======================== rendering ========================

    private void buildGrid() {
        farmGrid.getChildren().clear();
        int rows = farmMap.getHeight();
        int cols = farmMap.getWidth();
        tiles = new Tile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = new Tile(farmMap.getCell(r, c));
                tiles[r][c] = tile;
                farmGrid.add(tile.pane, c, r);
            }
        }
    }

    private void refreshAll() {
        if (tiles != null) {
            for (Tile[] row : tiles) {
                for (Tile t : row) t.style();
            }
        }
        dayField.setText("DAY " + gameManager.getCurrentDay());
        syncWeatherToggles();
        if (hovered != null) updateHover(hovered);
    }

    private void updateHover(Cell cell) {
        hovered = cell;
        cellLabel.setText("CELL : " + cell.getY() + "," + cell.getX());
        waterLabel.setText("WATER : " + cell.getMoistureLevel());
        nutrientLabel.setText("NUTR : " + cell.getNutrientLevel());
        Crop crop = cell.getCrop();
        if (crop == null) {
            cropLabel.setText("CROP : NONE");
        } else {
            cropLabel.setText("CROP : " + crop.getName()
                    + " [" + crop.getCurrentState() + "] "
                    + crop.getCurrentGrowthProgress() + "%");
        }
    }

    private void syncWeatherToggles() {
        WeatherType cur = gameManager.getCurrentWeather() != null
                ? gameManager.getCurrentWeather().getType() : null;
        sunWeatherButton.setSelected(cur == WeatherType.SUNNY);
        rainWeatherButton.setSelected(cur == WeatherType.RAINY);
        dryWeatherButton.setSelected(cur == WeatherType.DROUGHT);
    }

    private String weatherName() {
        return gameManager.getCurrentWeather() != null
                ? String.valueOf(gameManager.getCurrentWeather().getType()) : "-";
    }

    private void notify(String msg) {
        notificationField.setText(msg == null || msg.isEmpty() ? "..." : msg);
    }

    // ======================== icons ========================

    private void setupToolIcons() {
        hoeTool.setText(null);
        waterTool.setText(null);
        seedTool.setText(null);
        handTool.setText(null);
        fertilizerTool.setText(null);

        hoeTool.setGraphic(Assets.imageView(Assets.HOE_ICON, 48));
        waterTool.setGraphic(Assets.imageView(Assets.WATER_ICON, 48));
        seedTool.setGraphic(Assets.imageView(Assets.SEED_ICON, 48));
        handTool.setGraphic(Assets.imageView(Assets.HAND_ICON, 48));
        fertilizerTool.setGraphic(Assets.imageView(Assets.FERTILIZER_ICON, 48));
    }

    private void setupWeatherIcons() {
        sunWeatherButton.setText(null);
        rainWeatherButton.setText(null);
        dryWeatherButton.setText(null);
        sunWeatherButton.setGraphic(Assets.imageView(Assets.SUN_ICON, 42));
        rainWeatherButton.setGraphic(Assets.imageView(Assets.RAIN_ICON, 42));
        dryWeatherButton.setGraphic(Assets.imageView(Assets.DRY_ICON, 42));
    }

    // Restore the seed-bag hotbar button to its default icon (used when switching tools).
    private void resetSeedToolIcon() {
        seedTool.setGraphic(Assets.imageView(Assets.SEED_ICON, 48));
    }

    // Show the picked seed's sprite on the seed-bag hotbar button (falls back to the default icon).
    private void updateSeedToolIcon(String seedName) {
        Image sprite = null;
        Item item = player.getInventory().findByName(seedName);
        if (item != null) {
            sprite = Assets.getShopIcon(item);
        }
        if (sprite == null) {
            sprite = Assets.SEED_ICON;
        }
        seedTool.setGraphic(Assets.imageView(sprite, 48));
    }

    // ======================== cell tile ========================

    private final class Tile {
        final Cell cell;
        final StackPane pane = new StackPane();
        final ImageView pestMark = Assets.imageView(Assets.BEETLE_ICON, 20);
        final ImageView cropView = new ImageView();
        final ProgressBar progressBar = new ProgressBar();

        Tile(final Cell cell) {
            this.cell = cell;

            // crop state
            cropView.setFitWidth(54);
            cropView.setFitHeight(54);
            cropView.setPreserveRatio(true);
            cropView.setMouseTransparent(true);

            // progress bar
            progressBar.setPrefWidth(56);
            progressBar.setPrefHeight(8);

            progressBar.setMouseTransparent(true);

            StackPane.setAlignment(progressBar, Pos.BOTTOM_CENTER);

            progressBar.setTranslateY(-4);

            // pest
            pestMark.setMouseTransparent(true);
            StackPane.setAlignment(pestMark, Pos.TOP_RIGHT);

            // pane to add all
            pane.setMinSize(82, 82);
            pane.setPrefSize(82, 82);
            pane.setCursor(Cursor.HAND);
            pane.getChildren().addAll(cropView, progressBar, pestMark);

            pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) { onCellClick(cell); }
            });
            pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) { updateHover(cell); }
            });
            style();
        }

        void style() {
            String bg;
            if (cell.getCellState() == CellState.UNTILLED) {
                bg = "#5BA046";
            } else {
                bg = lerpHex("#C79A66", "#5A3A1E", cell.getMoistureLevel() / 100.0);
            }

            String border = "#3A2910";
            double bw = 1.2;
            Crop crop = cell.getCrop();
            if (crop != null && crop.getCurrentState() == GrowthState.HARVEST) {
                border = "#F6D46F"; bw = 3;
            } else if (crop != null && (crop.getCurrentState() == GrowthState.DEAD
                    || crop.getCurrentState() == GrowthState.ROTTEN)) {
                border = "#B23A2A"; bw = 2.5;
            } else if (cell.getCellState() == CellState.TILLED && cell.getNutrientLevel() > 60) {
                border = "#E0B83C"; bw = 2;
            }

            pane.setStyle("-fx-background-color: " + bg + ";"
                    + "-fx-background-radius: 6;"
                    + "-fx-border-color: " + border + ";"
                    + "-fx-border-width: " + bw + ";"
                    + "-fx-border-radius: 6;");

            if (crop == null) {
                cropView.setImage(null);
                progressBar.setVisible(false);
            } else {
                cropView.setImage(Assets.getCropSprite(crop));
                progressBar.setVisible(
                        crop.getCurrentState() != GrowthState.HARVEST
                                && crop.getCurrentState() != GrowthState.DEAD
                                && crop.getCurrentState() != GrowthState.ROTTEN
                );

                progressBar.setProgress(crop.getCurrentGrowthProgress() / 100.0);
            }
            pestMark.setVisible(cell.getPest() != null);
        }
    }

    private static String lerpHex(String a, String b, double t) {
        t = Math.max(0, Math.min(1, t));
        int[] ca = rgb(a), cb = rgb(b);
        int r  = (int) Math.round(ca[0] + (cb[0] - ca[0]) * t);
        int g  = (int) Math.round(ca[1] + (cb[1] - ca[1]) * t);
        int bl = (int) Math.round(ca[2] + (cb[2] - ca[2]) * t);
        return String.format("#%02X%02X%02X", r, g, bl);
    }

    private static int[] rgb(String hex) {
        return new int[]{
                Integer.parseInt(hex.substring(1, 3), 16),
                Integer.parseInt(hex.substring(3, 5), 16),
                Integer.parseInt(hex.substring(5, 7), 16)
        };
    }
}
