package view.screen_util;

import exception.SmartFarmException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public final class ScreensLoader {
    private ScreensLoader() {}

    /*
     - Loads an FXML file, registers its root with the given router under {@code key},
    and returns the loader so the caller can retrieve the controller.

     - param router: the router to register the screen with
     - param key:    the screen key (see {@link Screens})
     - param path:   the classpath-relative FXML path
     - return: the {@link FXMLLoader} after the screen has been loaded
     - throws: SmartFarmException if the FXML cannot be found or parsed
     */
    public static FXMLLoader load(SceneRouter router, String key, String path) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreensLoader.class.getResource(path));
            Parent root = loader.load();
            router.register(key, root);
            return loader;
        } catch (IOException | RuntimeException e) {
            throw new SmartFarmException("Failed to load FXML screen '" + key + "' from " + path, e);
        }
    }
}
