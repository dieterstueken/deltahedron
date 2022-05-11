package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableFloats;
import ditz.atrops.collections.IndexedList;
import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;

public class ObservablePoints extends IndexedList<Vertex> {

    AbstractObservableFloats values = new AbstractObservableFloats() {

        @Override
        public int size() {
            return 3 * ObservablePoints.this.size();
        }

        @Override
        public float get(int index) {
            Vertex v = ObservablePoints.super.get(index / 3);
            double coord = v.getCoord(index % 3);
            return (float) coord;
        }
    };

    @Override
    protected final void fireChange(boolean sizeChanged, int from, int to) {
        values.submitChange(sizeChanged, 3*from, 3*to);
    }

    public void addTarget(ObservableFloatArray target) {
        values.addTarget(target);
    }

    final Faces faces;

    public ObservablePoints(Faces faces) {
        this.faces = faces;
    }

    @Override
    public Vertex set(int index, Vertex vertex) {
        /**
         * element possibly gets a new index:
         * update all related faces.
         */
        try {
            return super.set(index, vertex);
        } finally {
            if(index!=vertex.getIndex())
                vertex.faces.forEach(faces::updateFace);
        }
    }

    public Vertex addPoint(Point3D point) {

        // reject too close points
        if(stream().anyMatch(v ->v.squareDist(point) * (1L << 32) < 1))
            return null;

        int id = size();
        Vertex vx = new Vertex(id, point);
        add(vx);

        int size = size();

        if(size==3) {
            Vertex v0 = get(0);
            Vertex v1 = get(1);

            faces.newFace(v0, v1, vx);
            faces.newFace(v1, v0, vx);
        } else if(size>3) {
            boolean connected = faces.addVertex(vx);
            if(!connected)
                throw new IllegalStateException("vertex not connected");
        }

        return vx;
    }
}
