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
        Face face = new Face(size(), v0, v1, v2);

        add(face);
        face.connect();

        return face;
    }

    public void updateFace(Face face) {
        int index = indexOf(face);
        update(index);
    }

    public void removeFace(Face face) {
        if(!remove(face))
            throw new IllegalStateException("face not registered");

        face.cutOff();
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
        removeFace(face);

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


    /**
     * Remove a given vertex with its faces.
     * Add new faces to close the gap.
     *
     * @param vx to cut off.
     */
    void cutOff(Vertex vx) {

        /**
         * Clip off edges until faces.size() < 3
         */
        for(int skip=0; vx.faces.size()>3; skip = nextClipOff(vx, skip)) {
            if(skip<0)
                throw new IllegalStateException("no next clip off edge found");
        }

        if(vx.faces.size()==3) {
            Vertex v0 = vx.getVertex(0, 1);
            Vertex v1 = vx.getVertex(1, 1);
            Vertex v2 = vx.getVertex(2, 1);

            // register new face below vx
            newFace(v0, v1, v2);

            // remove remaining faces from vertex.
            while(!vx.faces.isEmpty()) {
                Face f = vx.faces.get(0);
                removeFace(f);
            }
        }
    }

    /**
     * Analyze vertex and find the next edge that can be clipped off.
     * @param vx vertex to analyze.
     * @param skip position to start
     * @return an index clipped, or -1 if failed.
     */
    private int nextClipOff(Vertex vx, int skip) {

        int size = vx.faces.size();
        for(int i=0; i<size; ++i) {
            int k = i + skip;
            if(tryClipOff(vx, k)) {
                return k+1;
            }
        }

        return -1;
    }

    /**
     * Find if face(index) can be clipped off from a vertex.
     *
     * @param vx vertex to analyze.
     * @param index of face to analyze.
     *
     * @return if the action succeeds.
     */
    private boolean tryClipOff(Vertex vx, int index) {
        Face f0 = vx.faces.get(index);
        int i0 = f0.indexOf(vx);

        Face f1 = f0.getAdjacent(i0+1);
        int i1 = f1.indexOf(vx);

        Vertex v0 = f0.getPoint(i0+1);
        Vertex v1 = f0.getPoint(i0+2);
        Vertex v2 = f1.getPoint(i1+2);

        // candidate outer face (unconnected)
        Face fx = new Face(v0, v1, v2);

        int size = vx.faces.size();
        for(int i=0; i<size; ++i) {
            
            // seek 3 points beyond v0 v1 v2
            Face fi = vx.getFace(i+3);

            // not relevant
            if(fi==f0 || fi== f1)
                continue;

            int j = fi.indexOf(vx);
            Vertex vi = fi.getPoint(j+1);

            if(vi==v2)
                continue;

            double d = fx.dist(vi);

            // aboard action
            if(d>0)
                return false;
        }

        // all passed, drop f1 and f2

        vx.faces.remove(index);
        f0.cutOffEdge(i0);
        remove(f0);

        // expect f1 at deleted f0 position.
        if(vx.faces.get(index)==f1)
            vx.faces.remove(index);
        else {
            if (!vx.faces.remove(f1))
                throw new RuntimeException("face not found");
        }

        // disconnet from remaining points.
        f1.cutOffEdge(i1);
        remove(f1);

        add(fx);
        fx.connect();

        // create new intermediate inner face
        Face fy = new Face(v0, v2, vx);

        // replacing f1 and f2
        vx.faces.add(index, fy);
        fy.connectTo(v0);
        fy.connectTo(v2);
        add(fy);

        return true;
    }
}
