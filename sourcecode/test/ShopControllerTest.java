package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import view.assets.Assets;
import view.screen_util.SceneRouter;
import view.screen_util.ScreensLoader;
import view.screen_util.Screens;

public class GameTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Assets.load();

        SceneRouter router = ScreensLoader.loadAll();

        router.show(Screens.MAIN_MENU);
        Scene scene = new Scene(router.getRoot());

        stage.setScene(scene);
        stage.setTitle("Smart Farm");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}