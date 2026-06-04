package view.screen_controller;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

import view.assets.Assets;

public class InGameController {
    // TOOL BUTTONS
    @FXML private ToggleButton hoeTool;
    @FXML private ToggleButton waterTool;
    @FXML private ToggleButton seedTool;
    @FXML private ToggleButton shovelTool;
    @FXML private ToggleButton eraseTool;

    // WEATHER BUTTONS
    @FXML private ToggleButton sunWeatherButton;
    @FXML private ToggleButton rainWeatherButton;
    @FXML private ToggleButton dryWeatherButton;


    // INITIALIZE
    @FXML
    public void initialize() {
        setupToolIcons();
        setupWeatherIcons();
    }


    // SETUP TOOL ICONS
    private void setupToolIcons() {
        hoeTool.setText(null);
        waterTool.setText(null);
        seedTool.setText(null);
        shovelTool.setText(null);
        eraseTool.setText(null);

        hoeTool.setGraphic(Assets.imageView(Assets.HOE_ICON, 48));
        waterTool.setGraphic(Assets.imageView(Assets.WATER_ICON, 48));
        seedTool.setGraphic(Assets.imageView(Assets.SEED_ICON, 48));
        shovelTool.setGraphic(Assets.imageView(Assets.HAND_ICON, 48));
        eraseTool.setGraphic(Assets.imageView(Assets.FERTILIZER_ICON, 48));
    }

    // SETUP WEATHER ICONS
    private void setupWeatherIcons() {
        sunWeatherButton.setText(null);
        rainWeatherButton.setText(null);
        dryWeatherButton.setText(null);

        sunWeatherButton.setGraphic(Assets.imageView(Assets.SUN_ICON, 42));
        rainWeatherButton.setGraphic(Assets.imageView(Assets.RAIN_ICON, 42));
        dryWeatherButton.setGraphic(Assets.imageView(Assets.DRY_ICON, 42));
    }
}