package view.screen_util;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class SceneRouter {
    private final StackPane root = new StackPane();
    private final Map<String, Parent> screens = new HashMap<>();

    public StackPane getRoot() {
        return root;
    }

    public void register(String name, Parent screen) {
        screens.put(name, screen);
    }

    public void show(String name) throws Exception {
        Parent screen = screens.get(name);

        if (screen == null) {
            throw new Exception("Screen " + name + " not found");
        }

        root.getChildren().setAll(screen);
    }
}