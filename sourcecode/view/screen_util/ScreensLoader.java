package view.screen_util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import view.screen_controller.*;

public final class ScreensLoader {
    private ScreensLoader() {}

    public static SceneRouter loadAll() throws Exception {
        SceneRouter router = new SceneRouter();


        // ================= MAIN MENU ==================
        FXMLLoader mainMenuLoader = load(router, Screens.MAIN_MENU, Screens.MAIN_MENU_FXML);

        MainMenuController mainMenuController = mainMenuLoader.getController();
        mainMenuController.setRouter(router);

        // ================= HELP ====================
        FXMLLoader helpLoader = load(router, Screens.HELP, Screens.HELP_FXML);
        HelpController helpController = helpLoader.getController();
        helpController.setRouter(router);


        // ================= INGAME =================
        FXMLLoader ingameLoader = load(router, Screens.INGAME, Screens.INGAME_FXML);

        InGameController ingameController = ingameLoader.getController();
        ingameController.setRouter(router);

        // ================= SHOP =================
        FXMLLoader shopLoader = load(router, Screens.SHOP, Screens.SHOP_FXML);

        ShopController shopController = shopLoader.getController();
        shopController.setRouter(router);

        // ================= PAUSE ===================
        FXMLLoader pauseLoader = load(router, Screens.PAUSE, Screens.PAUSE_FXML);

        PauseController pauseController = pauseLoader.getController();
        pauseController.setRouter(router);

        // TODO: load seed bag (seed picker)

        return router;
    }

    private static FXMLLoader load(SceneRouter router, String key, String path) throws Exception {
        FXMLLoader loader = new FXMLLoader(ScreensLoader.class.getResource(path));

        Parent root = loader.load();
        router.register(key, root);

        return loader;
    }
}