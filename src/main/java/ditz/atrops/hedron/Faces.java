package ditz.atrops.hedron;

import javafx.geometry.Point3D;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  01.05.2022 13:05
 * modified by: $
 * modified on: $
 */
public class Faces extends ObservableFaces {

    public Face newFace(Vertex v0, Vertex v1, Vertex v2) {
        int id = size();
        Face face = new Face(id, v0, v1, v2);

        add(face);
        face.connect();

        return face;
    }

    public void updateFace(Face face) {
        int index = indexOf(face);
        update(index);
    }


    public boolean remove(Face face) {
        if(!super.remove(face))
            throw new IllegalStateException("face not registered");

        face.cutOff();
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        return obj instanceof Face && remove((Face) obj);
    }

    /**
     * Replace a given face and reconnect its edges to a given new Vertex.
     * For each edge find the adjacent face.
     * If the adjacent face is truly visible from the vertex, it is replaced, too.
     *
     * @param face to replace.
     * @param vertex to connect to.
     */
    void replace(Face face, Vertex vertex) {

        double dx = face.dist(vertex);
        if(dx<0)
            throw new IllegalArgumentException("face invisible by vertex");

        // unregister from its own vertices.
        remove(face);

        // process adjacent edges / faces.
        for(int j=0; j<3; ++j) {

            Face f = face.getAdjacent(j);
            if(f==null) // may be the adjacent face was already cut off.
                continue;

            double d = f.dist(vertex);

            // face is visible
            if(d>0) {
                replace(f, vertex);
            } else {

                // invisible or missing face, just add an edge
                Vertex p1 = face.points.get(j + 1);
                Vertex p2 = face.points.get(j + 2);
                newFace(p1, p2, vertex);
            }
        }
    }

    Face nextTo(Point3D point) {

        double dist = Double.NEGATIVE_INFINITY;
        Face faced = null;

        for (Face f : this) {
            double d = f.dist(point);
            // any nearer face
            if (faced == null || d > dist || (d == dist && f.det > faced.det)) {
                faced = f;
                dist = d;
            }
        }

        return faced;
    }

    public boolean addVertex(Vertex v) {
        Face face = nextTo(v.p0);
        if(face!=null)
            replace(face, v);

        return face != null;
    }
}
