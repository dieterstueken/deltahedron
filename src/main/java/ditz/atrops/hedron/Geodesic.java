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

    final Colors colors;

    final Faces faces;

    final Points points;

    public Geodesic() {
        colors = new Colors(256);
        faces = new Faces() {
            @Override
            public int getTex(Face f, int index) {
                return colors.getTex(f, index);
            }
        };

        points = new Points(faces);
    }

    public void clear() {
        faces.clear();
        points.clear();
    }

    public Vertex addPoint(double x, double y, double z) {
        final Point3D point = new Point3D(x, y, z);
        return points.addPoint(point);
    }

    public Vertex addPoint(Point3D point) {
        return points.addPoint(point);
    }

    public Vertex removePoint(int index) {
        return points.remove(index);
    }

    public Vertex removePoint(Vertex vertex) {
        int index = vertex.getIndex();

        if(vertex != points.get(index))
            throw new IllegalArgumentException("vertex not member");

        return points.remove(index);
    }

    public TriangleMesh createMesh() {
        TriangleMesh mesh = new TriangleMesh();

        mesh.getTexCoords().setAll(colors.coords);
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
