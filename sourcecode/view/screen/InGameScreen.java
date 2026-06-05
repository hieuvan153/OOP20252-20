package view.screen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class InGameScreen {
    private Parent root;

    public InGameScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/InGame.fxml"));
            root = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Parent getRoot() {
        return root;
    }
}