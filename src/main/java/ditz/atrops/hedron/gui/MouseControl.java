package ditz.atrops.hedron.gui;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;

class MouseControl {

    final SubScene scene;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    final Rotate xRotate;

    final Rotate yRotate;

    final ObjectProperty<Point3D> axisY;

    MouseControl(SubScene scene) {
        this.scene = scene;
        xRotate = new Rotate(0, Rotate.X_AXIS);
        yRotate = new Rotate(0, Rotate.Y_AXIS);

        axisY = yRotate.axisProperty();

        Node objects = scene.getRoot();

        objects.getTransforms().addAll(xRotate, yRotate);

        objects.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = xRotate.getAngle();
            anchorAngleY = yRotate.getAngle();
        });

        objects.setOnMouseDragged(event -> {
                xRotate.setAngle(anchorAngleX - (anchorY - event.getSceneY()));
                yRotate.setAngle(anchorAngleY + anchorX - event.getSceneX());
        });

        scene.addEventHandler(ScrollEvent.SCROLL, this::scroll);
    }

    void scroll(ScrollEvent event) {
        Node camera = scene.getCamera();
        double dist = camera.getTranslateZ();
        double delta = event.getDeltaY();
        dist += dist * delta / 200.0;
        camera.setTranslateZ(dist);
    }
}
