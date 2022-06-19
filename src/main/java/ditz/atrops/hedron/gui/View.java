package ditz.atrops.hedron.gui;

import ditz.atrops.hedron.Cube;
import ditz.atrops.hedron.RandomPoints;
import ditz.atrops.hedron.RandomSphere;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
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

    RandomSphere sphere = new RandomSphere(Cube.UNIT);

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

        BorderPane border = new BorderPane();



        border.setRight(controls());

        MeshView hedron = prepareHedron();
        border.setCenter(hedron);

        Scene scene = new Scene(border, WIDTH, HEIGHT, true);
        //scene.setFill(Color.SILVER);
        //Camera camera = new PerspectiveCamera(true);
        //camera.setNearClip(1);
        //camera.setFarClip(10000);
        //camera.translateZProperty().set(-1000);
        //scene.setCamera(camera);

        initMouseControl(hedron, scene, primaryStage);

        primaryStage.setTitle("DeltaHedrons");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    Node controls() {
        Node slider = new PlusMinusSlider();
        Node label = new Label("hello");
        VBox vbox = new VBox(label, slider);
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes
        return vbox;
    }

    private void initMouseControl(Node node, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        node.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            node.translateZProperty().set(node.getTranslateZ() + delta);
        });
    }

    private MeshView prepareHedron() {

        MeshView view = sphere.createView();
        view.getTransforms().add(new Scale(SIZE, SIZE, SIZE));
        view.setOnMouseClicked(e->{
            int selectedFace = e.getPickResult().getIntersectedFace();
            if(selectedFace>0) {
                ObservableFaceArray faces = ((TriangleMesh) view.getMesh()).getFaces();
                int colorId = faces.get(6 * selectedFace + 1);
                sphere.faces.updateColor(selectedFace);
                System.out.format("face: %d color: %d\n", selectedFace, colorId);
            }
        });


        //AnimationTimer timer = animation();
        //timer.start();

        return view;
    }

    private AnimationTimer animation() {
        return new AnimationTimer() {
            final RandomPoints rand = new RandomPoints();

            long until = 0;

            @Override
            public void handle(long now) {
                if(now>until) {

                    sphere.random(30, 50, 10);
                    System.out.print(sphere.points.size());
                    System.out.print(" ");
                    sphere.stat().showLine();

                    until = now + 300000000L;
                }
            }
        };
    }
}
