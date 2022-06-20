package ditz.atrops.hedron.gui;

import ditz.atrops.hedron.RandomPoints;
import ditz.atrops.hedron.RandomSphere;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 19.06.22
 * Time: 20:44
 */
public class SphereGroup {

    public static final float WIDTH = 600;
    public static final float HEIGHT = 500;

    final RandomSphere sphere;
    final MeshView hedron;
    final SubScene scene;
    
    SphereGroup(RandomSphere sphere) {
        this.sphere = sphere;
        this.hedron = prepareHedron(sphere);

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(50);
        light.setTranslateY(-300);
        light.setTranslateZ(-400);

        AmbientLight ambientLight = new AmbientLight(Color.WHITE);

        Group root = new Group(light, ambientLight, hedron);

        scene = new SubScene(root, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        scene.setCamera(new PerspectiveCamera());
        new MouseControl(scene);
    }

    private static final float SIZE = 100;

    private static MeshView prepareHedron(RandomSphere sphere) {

        MeshView view = sphere.createView();
        view.setTranslateX(SIZE);
        view.setTranslateY(SIZE);

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

    class MouseControl {

        private double anchorX, anchorY;
        private double anchorAngleX = 0;
        private double anchorAngleY = 0;
        private final DoubleProperty angleX = new SimpleDoubleProperty(0);
        private final DoubleProperty angleY = new SimpleDoubleProperty(0);

        MouseControl(SubScene scene) {
            Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
            Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
            xRotate.angleProperty().bind(angleX);
            yRotate.angleProperty().bind(angleY);

            hedron.getTransforms().addAll(xRotate, yRotate);

            hedron.setOnMousePressed(event -> {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                anchorAngleX = angleX.get();
                anchorAngleY = angleY.get();
            });

            hedron.setOnMouseDragged(event -> {
                angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
                angleY.set(anchorAngleY + anchorX - event.getSceneX());
            });

            scene.addEventHandler(ScrollEvent.SCROLL, this::scroll);
        }

        void scroll(ScrollEvent event) {
            double delta = event.getDeltaY();
            hedron.translateZProperty().set(hedron.getTranslateZ() + delta);
        }
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
