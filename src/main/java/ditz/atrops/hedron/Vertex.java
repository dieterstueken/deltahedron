package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 16.04.22
 * Time: 18:45
 */
public class Vertex {

    public final int index;

    public final Point3D p0;

    // ordered list of adjacent faces.
    List<Face> faces = new ArrayList<>();

    public Vertex(int i, Point3D p0) {
        this.index = i;
        this.p0 = p0;
    }

    public boolean addFace(Face newFace) {

        if(faces.isEmpty())
            return faces.add(newFace);

        int j = indexOn(newFace);
        Vertex v1 = newFace.get(j+1);
        Vertex v2 = newFace.get(j+2);

        for (int i = 0; i < faces.size(); i++) {
            Face face = faces.get(i);
            if(face==newFace)
                return false;

            j = indexOn(face);

            Vertex vx = face.get(j+1);
            if(vx==v2) {
                // insert before
                faces.add(i, newFace);
                return true;
            }

            vx = face.get(j+2);
            if(vx==v1) {
                // insert beyond
                faces.add(i+1, newFace);
                return true;
            }
        }

        // append to tail
        return faces.add(newFace);
    }

    public int indexOn(Face newFace) {
        int i = newFace.indexOf(this);
        if(i<0)
            throw new IllegalArgumentException("wrong face");
        return i;
    }

    public boolean removeFace(Face f) {
        return faces.remove(f);
    }

    public double pyth(Point3D p) {

        double x = p0.getX()-p.getX();
        double y = p0.getY()-p.getY();
        double z = p0.getZ()-p.getZ();

        return x*x + y*y +z*z;
    }

    public String toString() {
        return "V[" + index + "]";
    }
}
