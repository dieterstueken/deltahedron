package ditz.atrops.hedron.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  01.05.2022 18:41
 * modified by: $
 * modified on: $
 */
public class View extends Application {

    final SphereGroup sphere = new SphereGroup();

    Label label = new Label("hello");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DeltaHedrons");

        increment(0);

        BorderPane border = new BorderPane();

        border.setRight(controls());
        border.setCenter(sphere.view);

        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    Node controls() {

        VBox vbox = new VBox(label);
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        vbox.getChildren().add(increment(100));
        vbox.getChildren().add(increment(10));
        vbox.getChildren().add(increment(1));
        vbox.getChildren().add(increment(-1));
        vbox.getChildren().add(increment(-10));
        vbox.getChildren().add(increment(-100));

        vbox.getChildren().add(dyer());

        return vbox;
    }

    Button increment(int count) {
        var button = new Button(Integer.toString(count));
        button.setOnAction(e -> incrementPoints(count));
        return button;
    }

    Button dyer() {
        var button = new Button("dye");
        button.setOnAction(e -> sphere.dye());
        return button;
    }

    void incrementPoints(int count) {
        sphere.incrementPoints(count);

        label.setText(Integer.toString(sphere.sphere.points.size()));

        sphere.dye();
    }
}
