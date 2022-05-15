package ditz.atrops.hedron;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  01.05.2022 18:41
 * modified by: $
 * modified on: $
 */
public class View extends Application {

    RandomSphere sphere = new RandomSphere(4);

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
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

        Group world = new Group(prepareHedron());
        world.getTransforms().add(new Scale(SIZE, SIZE, SIZE));

        Scene scene = new Scene(world, WIDTH, HEIGHT, true);
        //scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        initMouseControl(world, scene, primaryStage);

        primaryStage.setTitle("DeltaHedrons");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initMouseControl(Group group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
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
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }

    private Node prepareHedron() {

        Palette palette = Palette.DEFAULT;

        //s.addPoints(Cube.UNIT);
        TriangleMesh mesh = sphere.createMesh();
        mesh.getTexCoords().setAll(palette.getTextPoints());

        MeshView view = new MeshView(mesh);

        view.setMaterial(palette.createMaterial());
        view.setDrawMode(DrawMode.FILL);
        view.setCullFace(CullFace.NONE);
        view.setOnMouseClicked(e->{
            int selectedFace = e.getPickResult().getIntersectedFace();
            if(selectedFace>0) {
                ObservableFaceArray faces = ((TriangleMesh) view.getMesh()).getFaces();
                int colorId = faces.get(6 * selectedFace + 1);
                sphere.faces.updateColor(selectedFace);
                System.out.format("face: %d color: %d\n", selectedFace, colorId);
            }
        });

        AnimationTimer timer = new AnimationTimer() {
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
        timer.start();

        return view;
    }
}
