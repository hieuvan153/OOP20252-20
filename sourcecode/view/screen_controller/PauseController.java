package view.screen_controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import view.screen_util.SceneRouter;
import view.screen_util.Screens;

public final class PauseController {
    @FXML
    private Button btnResume;
    @FXML
    private Button btnMainMenu;
    @FXML
    private Button btnQuitGame;

    private SceneRouter router;

    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @FXML
    private void onResume() {
        if (router != null) {
            try {
                router.show(Screens.INGAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onMainMenu() {
        if (router != null) {
            try {
                router.show(Screens.MAIN_MENU);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onQuit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("All unsaved progress will be lost.");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
}
