package view.screen_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import view.screen_util.SceneRouter;
import view.screen_util.Screens;

public class ShopController {
    private SceneRouter router;

    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @FXML
    public void backToGame(ActionEvent event) {
        if (router != null) {
            try {
                router.show(Screens.INGAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}