package ditz.atrops.hedron;

import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;


/**
 * version:     $
 * created by:  d.stueken
 * created on:  08.05.2022 16:42
 * modified by: $
 * modified on: $
 */
public class Geodesic implements Faces {

    final ObservablePoints points = new ObservablePoints();
    final ObservableFaces faces = new ObservableFaces();
    int faceId = 0;

    public void clear() {
        faces.clear();
        points.clear();
        faceId = 0;
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
        if(!faces.remove(face))
            throw new IllegalStateException("face not registered");

        face.cutOff();
    }

    @Override
    public Face newFace(Vertex v0, Vertex v1, Vertex v2) {
        Face face = new Face(++faceId, v0, v1, v2);

        faces.add(face);
        face.connect();

        return face;
    }

    public TriangleMesh createMesh() {
        TriangleMesh mesh = new TriangleMesh();
        points.addTarget(mesh.getPoints());
        faces.addTarget(mesh.getFaces());
        return mesh;
    }

    public Stat stat() {
        Stat stat = new Stat();
        points.forEach(p->stat.add(p.faces.size()));
        return stat;
    }
}
