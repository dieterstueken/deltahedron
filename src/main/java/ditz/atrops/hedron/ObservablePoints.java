package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableFloats;
import ditz.atrops.collections.IndexedList;
import javafx.collections.ObservableFloatArray;

import java.util.function.Predicate;

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
        this.target = target;
    }

    private ObservableFloatArray target = null;

    @Override
    public boolean verify(Predicate<? super Vertex> verify) {

        assert verifyTarget();

        return super.verify(verify);
    }

    private boolean verifyTarget() {
        if(target!=null) {
            int size = values.size();
            if(target.size() != size) {
                assert false : "size invalid";
                return false;
            }


            for(int i=0; i<size; ++i) {
                if(target.get(i) != values.get(i)) {
                    assert false : "value invalid";
                    return false;
                }
            }
        }

        return true;
    }
}
