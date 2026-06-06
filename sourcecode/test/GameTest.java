package test;

import controller.actionmanager.PlayerController;
import controller.democontroller.DemoController;
import controller.lifecyclemanager.GameManager;
import controller.shopmanager.ShopController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.player_inventory.Player;
import model.soil.FarmMap;
import view.assets.Assets;
import view.screen_controller.HelpController;
import view.screen_controller.InGameController;
import view.screen_controller.MainMenuController;
import view.screen_controller.PauseController;
import view.screen_controller.ShopViewController;
import view.screen_util.SceneRouter;
import view.screen_util.ScreensLoader;
import view.screen_util.Screens;

public class GameTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Assets.load();

        SceneRouter router = new SceneRouter();

        // 1. Khởi tạo các Domain Objects (Models & Managers)
        GameManager gameManager = GameManager.getInstance();
        Player player = new Player();
        FarmMap farmMap = new FarmMap();
        PlayerController playerController = new PlayerController(player, gameManager, farmMap.getGrid());
        DemoController demoController = new DemoController(gameManager, farmMap);
        ShopController domainShop = new ShopController(player, gameManager, farmMap.getGrid());

        // 2. Load từng screen và inject dependency
        MainMenuController mainMenu = ScreensLoader
                .load(router, Screens.MAIN_MENU, Screens.MAIN_MENU_FXML)
                .getController();
        mainMenu.init(router, null); // GameLauncher = null tạm thời khi test

        HelpController help = ScreensLoader
                .load(router, Screens.HELP, Screens.HELP_FXML)
                .getController();
        help.setRouter(router);

        InGameController inGame = ScreensLoader
                .load(router, Screens.INGAME, Screens.INGAME_FXML)
                .getController();
        inGame.setRouter(router);
        // Inject game session vào InGame
        inGame.setGame(farmMap, player, gameManager, playerController, demoController);

        ShopViewController shop = ScreensLoader
                .load(router, Screens.SHOP, Screens.SHOP_FXML)
                .getController();
        shop.setRouter(router);
        // Inject shop session vào ShopView
        shop.setShop(player, domainShop);

        PauseController pause = ScreensLoader
                .load(router, Screens.PAUSE, Screens.PAUSE_FXML)
                .getController();
        pause.init(router, null); // GameLauncher = null tạm thời khi test

        router.show(Screens.MAIN_MENU);

        Scene scene = new Scene(router.getRoot());
        stage.setScene(scene);
        stage.setTitle("Smart Farm (Test Mode)");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
