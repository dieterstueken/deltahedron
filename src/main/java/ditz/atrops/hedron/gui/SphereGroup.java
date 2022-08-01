package ditz.atrops.hedron.gui;

import ditz.atrops.hedron.Face;
import ditz.atrops.hedron.RandomPoints;
import ditz.atrops.hedron.RandomSphere;
import ditz.atrops.hedron.Tetraeder;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 19.06.22
 * Time: 20:44
 */
public class SphereGroup {

    public static final float WIDTH = 600;
    public static final float HEIGHT = 500;

    final PerspectiveCamera  camera = new PerspectiveCamera(true);

    final RandomSphere sphere;
    final MeshView hedron;
    final SubScene scene;

    final GraphGroup graph = new GraphGroup();

    final HBox box;

    SphereGroup() {
        this.sphere = new RandomSphere(Tetraeder.UNIT);
        this.hedron = prepareHedron(sphere);

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(50);
        light.setTranslateY(-300);
        light.setTranslateZ(-400);

        AmbientLight ambientLight = new AmbientLight(Color.WHITE);

        Group root = new Group(light, ambientLight, hedron);

        camera.setTranslateZ(-10);

        scene = new SubScene(root, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        scene.setCamera(camera);
        new MouseControl(scene);

        box = new HBox(scene, graph.canvas);

        graph.draw(sphere);
    }

    private static MeshView prepareHedron(RandomSphere sphere) {

        MeshView view = sphere.createView();

        view.setOnMouseClicked(e->{
            int selectedFace = e.getPickResult().getIntersectedFace();
            if(selectedFace>=0) {
                sphere.faces.updateColor(selectedFace);
                print(sphere.faces.get(selectedFace));
            }
        });

        //AnimationTimer timer = animation();
        //timer.start();

        return view;
    }

    static void print(Face face) {
        System.out.format("face: %d colors: %d %02x:%02x:%02x\n",
                face.getIndex(),
                face.getColor(),
                face.getColor(0),
                face.getColor(1),
                face.getColor(2)
        );
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
            double dist = camera.getTranslateZ();
            double delta = event.getDeltaY();
            dist += dist*delta/200.0;
            camera.translateZProperty().set(dist);
        }
    }

    void incrementPoints(int count) {

        for (int i = 0; i < Math.abs(count); ++i) {
            if (count > 0)
                sphere.addPoint();
            else
                sphere.removePoint();
        }

        graph.draw(sphere);

        sphere.stat().showLine();
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
