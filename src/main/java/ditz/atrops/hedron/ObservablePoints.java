package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableFloats;
import ditz.atrops.collections.ObservableArrayList;
import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;

public class ObservablePoints extends ObservableArrayList<Vertex> {

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
    public Vertex set(int index, Vertex element) {
        return super.set(index, element);
        // update faces
    }

    @Override
    protected final void fireChange(boolean sizeChanged, int from, int to) {
        values.submitChange(sizeChanged, 3*from, 3*to);
    }

    public void addTarget(ObservableFloatArray target) {
        values.addTarget(target);
    }

    int id = 0;

    public Vertex addPoint(Point3D point) {
        Vertex v = new Vertex(id++, point);
        add(v);
        return v;
    }
}
