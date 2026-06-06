package test;

import controller.actionmanager.PlayerController;
import controller.democontroller.DemoController;
import controller.environmentcontroller.EnvironmentController;
import controller.farmcontroller.FarmController;
import controller.lifecyclemanager.GameManager;
import controller.shopmanager.ShopController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.player_inventory.Player;
import model.soil.FarmMap;
import view.assets.Assets;
import view.screen_controller.HelpController;
import view.screen_controller.InGameController;
import view.screen_controller.MainMenuController;
import view.screen_controller.PauseController;
import view.screen_controller.ShopViewController;
import view.screen_util.GameLauncher;
import view.screen_util.SceneRouter;
import view.screen_util.Screens;
import view.screen_util.ScreensLoader;

import java.net.URL;

/**
 * JavaFX entry point and composition root.
 *
 * <p>Builds the {@code model.*} + {@code controller.*} stack (which contain no
 * UI imports) and wires them into the FXML screens via a {@link SceneRouter}.
 * Session-less screens (main menu / help / pause) are loaded once; the gameplay
 * screens (in-game / shop) are rebuilt around fresh model objects whenever a new
 * farm is started.</p>
 */
public class MainTest extends Application implements GameLauncher {
    private SceneRouter router;
    private MainMenuController menuController;

    // --- current session (null until the first new farm) ---
    private Player player;
    private FarmMap farmMap;
    private GameManager gameManager;
    private PlayerController playerController;
    private ShopController shopController;
    private DemoController demoController;
    // held so the auto-registered DayObservers stay referenced
    private EnvironmentController environmentController;
    private FarmController farmController;

    @Override
    public void start(Stage stage) {
        // Single view-layer error boundary: surface anything that escapes to the
        // JavaFX thread as a friendly dialog instead of a silent console trace.
        Thread.currentThread().setUncaughtExceptionHandler((t, ex) -> {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Smart Farm");
            alert.setHeaderText("Unexpected error");
            alert.setContentText(String.valueOf(ex.getMessage()));
            alert.showAndWait();
        });

        Assets.load();
        router = new SceneRouter();

        // session-less screens, registered once
        FXMLLoader menuLoader = ScreensLoader.load(router, Screens.MAIN_MENU, Screens.MAIN_MENU_FXML);
        menuController = menuLoader.getController();
        menuController.init(router, this);
        menuController.setContinueEnabled(false);

        FXMLLoader helpLoader = ScreensLoader.load(router, Screens.HELP, Screens.HELP_FXML);
        HelpController help = helpLoader.getController();
        help.setRouter(router);

        FXMLLoader pauseLoader = ScreensLoader.load(router, Screens.PAUSE, Screens.PAUSE_FXML);
        PauseController pause = pauseLoader.getController();
        pause.init(router, this);

        Scene scene = new Scene(router.getRoot(), 1280, 720);
        URL css = getClass().getResource(Screens.STYLESHEET);
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Smart Farm");
        stage.setResizable(false);
        stage.show();

        router.show(Screens.MAIN_MENU);
    }

    // ---------------- GameLauncher ----------------

    @Override
    public void startNewFarm() {
        gameManager = GameManager.getInstance();
        gameManager.reset();

        farmMap = new FarmMap();
        player = new Player();

        // controllers self-register with the GameManager as DayObservers
        environmentController = new EnvironmentController(gameManager, farmMap);
        farmController = new FarmController(gameManager, farmMap);
        shopController = new ShopController(player, gameManager, farmMap.getGrid());
        playerController = new PlayerController(player, gameManager, farmMap.getGrid());
        demoController = new DemoController(gameManager, farmMap);

        FXMLLoader shopLoader = ScreensLoader.load(router, Screens.SHOP, Screens.SHOP_FXML);
        ShopViewController shopView = shopLoader.getController();
        shopView.setRouter(router);
        shopView.setShop(player, shopController);

        FXMLLoader ingameLoader = ScreensLoader.load(router, Screens.INGAME, Screens.INGAME_FXML);
        InGameController ingame = ingameLoader.getController();
        ingame.setRouter(router);
        ingame.setGame(farmMap, player, gameManager, playerController, demoController);

        router.show(Screens.INGAME);
    }

    @Override
    public void continueFarm() {
        if (!hasSession()) {
            startNewFarm();
        } else {
            router.show(Screens.INGAME);
        }
    }

    @Override
    public boolean hasSession() {
        return player != null && farmMap != null;
    }

    @Override
    public void returnToMainMenu() {
        if (menuController != null) menuController.setContinueEnabled(hasSession());
        router.show(Screens.MAIN_MENU);
    }

    @Override
    public void quit() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}