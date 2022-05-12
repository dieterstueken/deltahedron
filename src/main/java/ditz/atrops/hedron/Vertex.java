package ditz.atrops.hedron;

import ditz.atrops.collections.Indexed;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 16.04.22
 * Time: 18:45
 */
public class Vertex extends Indexed {

    public final Point3D p0;

    // ordered list of adjacent faces.
    List<Face> faces = new ArrayList<>() {

        /**
         * Make index cyclic.
         *
         * @param index to get
         * @return index between 0 and size() exclusive.
         */
        private int mod(int index) {
            int size = size();
            index %= size;
            if(index<0)
                index = size - index;
            return index;
        }

        @Override
        public Face get(int index) {
            return super.get(mod(index));
        }

        @Override
        public Face remove(int index) {
            return super.remove(mod(index));
        }
    };

    public Vertex(Point3D p0) {
        this(-1, p0);
    }

    public Vertex(int i, Point3D p0) {
        super(i);
        this.p0 = p0;
    }

    public double getCoord(int i) {
        // modulo, even for negative values
        return switch (i % 3) {
            case -2, 1 -> p0.getY();
            case -1, 2 -> p0.getZ();
            default -> p0.getX();
        };
    }

    public Face getFace(int i) {
        return faces.get(i);
    }

    /**
     * Return the vertex of face[i] j positions from own position.
     * @param i face index.
     * @param j point index.
     * @return the vertex at the given position.
     */
    public Vertex getVertex(int i, int j) {
        Face f0 = faces.get(i);
        int k = f0.indexOf(this);
        return f0.getPoint(j+k);
    }

    public boolean addFace(Face newFace) {

        if(faces.isEmpty())
            return faces.add(newFace);

        int j = indexOn(newFace);
        Vertex v1 = newFace.getPoint(j+1);
        Vertex v2 = newFace.getPoint(j+2);

        for (int i = 0; i < faces.size(); i++) {
            Face face = faces.get(i);
            if(face==newFace)
                return false;

            j = indexOn(face);

            Vertex vx = face.getPoint(j+1);
            if(vx==v2) {
                // insert before
                faces.add(i, newFace);
                return true;
            }

            vx = face.getPoint(j+2);
            if(vx==v1) {
                // insert beyond
                faces.add(i+1, newFace);
                return true;
            }
        }

        // append to tail
        return faces.add(newFace);
    }

    /**
     * Find if the given face contains this vertex at a given position.
     *
     * @param face to inspect.
     * @return index of this vertex on face or -1 if not related.
     */
    public int indexOn(Face face) {
        return face.indexOf(this);
    }

    public boolean removeFace(Face f) {
        return faces.remove(f);
    }

    public double squareDist(Point3D p) {

        double x = p0.getX()-p.getX();
        double y = p0.getY()-p.getY();
        double z = p0.getZ()-p.getZ();

        return x*x + y*y +z*z;
    }
}
