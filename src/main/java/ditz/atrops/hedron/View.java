package ditz.atrops.hedron;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.PhongMaterial;
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

        UnitSphere s = new UnitSphere();
        s.generate(17);

        //s.addPoints(Cube.UNIT);
        TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
        mesh.getTexCoords().setAll(palette.getTextPoints());
        mesh.getPoints().setAll(s.getPoints());
        mesh.getFaces().setAll(s.getFaces());

        MeshView view = new MeshView(mesh);

        view.setMaterial(palette.createMaterial());
        view.setDrawMode(DrawMode.FILL);
        view.setCullFace(CullFace.NONE);
        view.setOnMouseClicked(e->{
            int selectedFace = e.getPickResult().getIntersectedFace();
            if(selectedFace>0) {
                ObservableFaceArray faces = ((TriangleMesh) view.getMesh()).getFaces();
                int colorId = faces.get(6 * selectedFace + 1);
                System.out.format("color: %d\n", colorId);
            }
        });

        return view;
    }

    private static Sphere prepareEarth(float size) {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(View.class.getResourceAsStream("/earth-d.jpg")));
        //earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/earth/earth-l.jpg")));
        //earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("/resources/earth/earth-s.jpg")));
        //earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream("/resources/earth/earth-n.jpg")));

        Sphere sphere = new Sphere(size);
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }
}
