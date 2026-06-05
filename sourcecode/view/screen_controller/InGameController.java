package view.screen_controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.assets.Assets;
import view.screen_util.SceneRouter;
import view.screen_util.Screens;

public class InGameController {
    // TOOL BUTTONS
    @FXML private ToggleButton seedTool;
    @FXML private ToggleButton hoeTool;
    @FXML private ToggleButton waterTool;
    @FXML private ToggleButton handTool;
    @FXML private ToggleButton fertilizerTool;

    // WEATHER BUTTONS
    @FXML private ToggleButton sunWeatherButton;
    @FXML private ToggleButton rainWeatherButton;
    @FXML private ToggleButton dryWeatherButton;

    @FXML private BorderPane rootPane;

    private SceneRouter router;

    // ======================== INITIALIZE ========================
    @FXML
    private void initialize() {
        setupToolIcons();
        setupWeatherIcons();

        setKeyPressed();
    }

    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @FXML
    private void setKeyPressed() {
        // Esc to Pause
        rootPane.setFocusTraversable(true);
        Platform.runLater(() -> rootPane.requestFocus());

        rootPane.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> {
                    if (router.hasOverlay()) {
                        router.closeOverlay();
                    } else {
                        router.showOverlay(router.getScreen(Screens.PAUSE));
                    }
                }
            }
        });
    }

    @FXML
    private void openShop(ActionEvent event) {
        try {
            router.show(Screens.SHOP);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // SETUP TOOL ICONS
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