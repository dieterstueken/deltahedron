package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableIntegers;
import ditz.atrops.collections.IndexedList;
import javafx.collections.ObservableIntegerArray;
import javafx.geometry.Point3D;

public class ObservableFaces extends IndexedList<Face> implements Faces {

    AbstractObservableIntegers values = new AbstractObservableIntegers() {

        @Override
        public int size() {
            return 6*ObservableFaces.super.size();
        }

        @Override
        public int get(int index) {
            Face f = ObservableFaces.super.get(index/6);

            return switch (index % 6) {
                case 0 -> f.v0.getIndex();
                case 2 -> f.v1.getIndex();
                case 4 -> f.v2.getIndex();
                // 1 3 5 common color index
                default -> 3 + (f.color)%6;
            };
        }
    };

    public void updateColor(int i) {
        Face face = get(i);
        int color = face.color;
        face.color = (color+1)%6;
        fireChange(i);
    }

    @Override
    protected final void fireChange(boolean sizeChanged, int from, int to) {
        values.submitChange(sizeChanged, 6*from, 6*to);
    }

    protected final void fireChange(int i) {
        fireChange(false, i, i+1);
    }

    public void addTarget(ObservableIntegerArray target) {
        values.addTarget(target);
    }

    public Face newFace(Vertex v0, Vertex v1, Vertex v2) {
        int id = size();
        Face face = new Face(id, v0, v1, v2);

        add(face);
        face.connect();

        return face;
    }

    @Override
    public Face addFace(Face face) {
        boolean added = add(face);
        if(!added)
            throw new IllegalStateException("face already registered");
        face.connect();
        return face;
    }

    @Override
    public void updateFace(Face face) {
        int index = indexOf(face);
        update(index);
    }


    @Override
    public boolean remove(Object obj) {
        return obj instanceof Face && removeFace((Face) obj)!=null;
    }

    public Face removeFace(Face face) {
        if(!super.remove(face))
            throw new IllegalStateException("face not registered");

        face.cutOff();
        return face;
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

    @Override
    public boolean addVertex(Vertex v) {
        Face face = nextTo(v.p0);
        if(face!=null)
            replace(face, v);

        return face != null;
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
            throw new IllegalArgumentException("add outside vertex");

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
}
