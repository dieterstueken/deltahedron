package ditz.atrops.hedron.gui;

import ditz.atrops.hedron.Cube;
import ditz.atrops.hedron.RandomSphere;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PlusMinusSlider;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  01.05.2022 18:41
 * modified by: $
 * modified on: $
 */
public class View extends Application {

    final RandomSphere sphere = new RandomSphere(Cube.UNIT);

    private static final float WIDTH = 600;
    private static final float HEIGHT = 500;
    private static final float SIZE = 100;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DeltaHedrons");

        BorderPane border = new BorderPane();

        border.setRight(controls());
        border.setCenter(sphere());

        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    Node sphere() {
        return new SphereGroup(sphere).scene;
    }

    Node controls() {
        Node slider = new PlusMinusSlider();
        Node label = new Label("hello");
        VBox vbox = new VBox(label, slider);
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes
        return vbox;
    }
}
