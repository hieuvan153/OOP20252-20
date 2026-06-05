package view.screen_controller;

import javafx.event.ActionEvent;
import view.screen_util.SceneRouter;
import view.screen_util.Screens;

import java.io.IOException;

public class ShopController {
    private SceneRouter router;

    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    public void backToGame(ActionEvent event) throws Exception {
        try {
            router.show(Screens.INGAME);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}