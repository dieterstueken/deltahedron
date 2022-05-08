package ditz.atrops.hedron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;


/**
 * version:     $
 * created by:  d.stueken
 * created on:  08.05.2022 16:42
 * modified by: $
 * modified on: $
 */
public class Geodesic implements Faces {

    final ObservableList<Vertex> points = FXCollections.observableArrayList();
    final ObservableList<Face> faces = FXCollections.observableArrayList();
    int faceId = 0;

    public ObservableFaces getFaces() {
        return new ObservableFaces(faces);
    }

    public ObservablePoints getPoints() {
        return new ObservablePoints(points);
    }

    public Vertex addPoint(Point3D point) {
        int i = points.size();
        Vertex v = new Vertex(i, point);
        points.add(v);
        return v;
    }

    public void addFace(Face face) {
        boolean added = faces.add(face);
        if(!added)
            throw new IllegalStateException("face already registered");
        face.connect();
    }

    public void removeFace(Face face) {
        boolean removed = faces.remove(face);
        face.cutOff();

        if(!removed)
            throw new IllegalStateException("face not registered");
    }

    @Override
    public Face newFace(Vertex v0, Vertex v1, Vertex v2) {
        Face face = new Face(++faceId, v0, v1, v2);

        faces.add(face);
        face.connect();

        return face;
    }
}
