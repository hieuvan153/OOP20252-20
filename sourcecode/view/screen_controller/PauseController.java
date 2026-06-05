package view.screen_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public final class PauseController {
    @FXML
    private Button btnResume;
    @FXML
    private Button btnMainMenu;
    @FXML
    private Button btnQuitGame;

    private Runnable resumeAction = new Runnable() {
        @Override
        public void run() {}
    };

    private Runnable mainMenuAction = new Runnable() {
        @Override
        public void run() {}
    };

    private Runnable quitAction = new Runnable() {
        @Override
        public void run() {}
    };

    public void init(Runnable onResume, Runnable onMainMenu, Runnable onQuit) {
        if (onResume   != null) this.resumeAction   = onResume;
        if (onMainMenu != null) this.mainMenuAction = onMainMenu;
        if (onQuit     != null) this.quitAction     = onQuit;
    }

    @FXML
    private void onResume()   { resumeAction.run(); }
    @FXML
    private void onMainMenu() { mainMenuAction.run(); }
    @FXML
    private void onQuit()     { quitAction.run(); }

}
