package view.screen_controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;


public final class SeedPickerController {

    @FXML
    private VBox list;
    @FXML
    private Button btnCancel;

    private Consumer<String> onPick = new Consumer<String>() {
        @Override
        public void accept(String s) {}
    };

    private Runnable onClose = new Runnable() {
        @Override
        public void run() {}
    };


    // Removed Player dependency from the init method
    public void init(Consumer<String> onPick, Runnable onClose) {
        if (onPick != null) this.onPick = onPick;
        if (onClose != null) this.onClose = onClose;

        populateDummyData(); // Call mock data generator
    }

    // Generate mock data for UI testing
    private void populateDummyData() {
        if (list != null) {
            list.getChildren().clear();

            // Add 3 mock seed rows to test the UI layout
            list.getChildren().add(makeRow("Tomato Seed", 5));
            list.getChildren().add(makeRow("Carrot Seed", 12));

            // Test case: quantity is 0 (USE button should be disabled)
            list.getChildren().add(makeRow("Potato Seed", 0));

            /*
             - (Commented out) To test the empty state UI:
             - Label empty = new Label("No seeds yet — visit the Shop!");
             - empty.getStyleClass().add("seed-empty");
             - list.getChildren().add(empty);
             */
        }
    }

    // Create a single row displaying seed information
    private HBox makeRow(String seedName, int qty) {
        // MOCK ICON: Use a brown square region instead of the real SpriteView
        Region iconPlaceholder = new Region();
        iconPlaceholder.setMinSize(40, 40);
        iconPlaceholder.setStyle("-fx-background-color: #8B4513; -fx-background-radius: 8;");

        // Seed name and quantity
        Label name = new Label(seedName + "  ×" + qty);
        name.getStyleClass().add("seed-name");

        // Spacer to push the USE button to the far right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // USE button setup
        Button use = new Button("USE");
        use.getStyleClass().add("btn-gold");
        use.setDisable(qty <= 0); // Disable button if out of seeds
        use.setOnAction(e -> {
            onPick.accept(seedName);
            onClose.run();
            System.out.println("Selected seed: " + seedName); // Print to console for testing
        });

        // Wrap everything in an HBox row
        HBox row = new HBox(12, iconPlaceholder, name, spacer, use);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("seed-row");
        return row;
    }

    // Handle CANCEL button action
    @FXML
    private void onCancel() {
        System.out.println("Cancel button clicked");
        onClose.run();
    }
}
