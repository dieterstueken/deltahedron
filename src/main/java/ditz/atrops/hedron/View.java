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

import java.io.InputStream;
import java.util.Collection;

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
        MeshView view = new MeshView(createRandomMesh(4));
        view.setMaterial(createMaterial());
        view.setDrawMode(DrawMode.FILL);
        view.setCullFace(CullFace.BACK);
        return view;
    }

    private static TriangleMesh createRandomMesh(int count) {
        UnitSphere s = new UnitSphere();
        s.generate(count);
        //s.addPoints(Cube.UNIT);
        return createMesh(s);
    }

    private static TriangleMesh createMesh(UnitSphere s) {
        TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
        mesh.getTexCoords().setAll(getColors());
        mesh.getPoints().setAll(getCoords(s.points));
        mesh.getFaces().setAll(getFaces(s.faces));
        return mesh;
    }

    private static float scale(double value) {
        return (float) value;
    }

    private static float[] getCoords(Collection<Vertex> points) {
        float[] coords = new float[3*points.size()];

        for (Vertex v : points) {
            int i = v.index;
            coords[3*i+0] = scale(v.p0.getX());
            coords[3*i+1] = scale(v.p0.getY());
            coords[3*i+2] = scale(v.p0.getZ());
        }

        return coords;
    }

    static int [] getFaces(Collection<Face> faces) {
        int i = faces.size();
        int[] idx = new int[6*i];
        
        i=0;
        for (Face f : faces) {
            if(i>=idx.length)
                break;

            idx[i + 0] = f.v0.index;
            idx[i + 2] = f.v1.index;
            idx[i + 4] = f.v2.index;

            int color = f.hashCode() % 4;
            idx[i + 1] = color;
            idx[i + 3] = color;
            idx[i + 5] = color;

            i += 6;
        }

        return idx;
    }

    static float [] getColors() {
        float[] colors = new float[8];
        for(int i=0; i<4; ++i) {
            colors[2*i+0] = (i + 0.5F)/4;
            colors[2*i+1] = 0.5F;
        }

        return colors;
    }

    static PhongMaterial createMaterial() {
        PhongMaterial mat = new PhongMaterial();

        InputStream colors = View.class.getResourceAsStream("/colors.png");

        mat.setDiffuseMap(new Image(colors));
        return mat;
    }

    private Sphere prepareEarth(float size) {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/earth-d.jpg")));
        //earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/earth/earth-l.jpg")));
        //earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("/resources/earth/earth-s.jpg")));
        //earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream("/resources/earth/earth-n.jpg")));

        Sphere sphere = new Sphere(size);
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }
}
