package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableFloats;
import ditz.atrops.collections.ObservableArrayList;
import javafx.collections.ObservableFloatArray;

public class ObservablePoints extends ObservableArrayList<Vertex> {

    AbstractObservableFloats points = new AbstractObservableFloats() {

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
        points.submitChange(sizeChanged, 3*from, 3*to);
    }

    public void addTarget(ObservableFloatArray target) {
        points.addTarget(target);
    }
}
