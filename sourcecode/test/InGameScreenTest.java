package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import view.assets.Assets;
import view.screen.InGameScreen;

public class InGameScreenTest extends Application {

    @Override
    public void start(Stage stage) {
        Assets.load();

        InGameScreen screen =
                new InGameScreen();

        Scene scene =
                new Scene(screen.getRoot());

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}