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
public class Geodesic  {

    final Faces faces;

    final Points points;

    public Geodesic() {
        faces = new Faces();
        points = new Points(faces);
    }

    public void clear() {
        faces.clear();
        points.clear();
    }

    public Vertex addPoint(Point3D point) {
        return points.addPoint(point);
    }

    public Vertex removePoint(int index) {
        Vertex vertex = points.get(index);
        points.remove(vertex);
        return vertex;
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
