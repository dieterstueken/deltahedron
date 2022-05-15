package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableIntegers;
import ditz.atrops.collections.IndexedList;
import javafx.collections.ObservableIntegerArray;

import java.util.function.Predicate;

public class ObservableFaces extends IndexedList<Face> {

    AbstractObservableIntegers values = new AbstractObservableIntegers() {

        @Override
        public int size() {
            return 6*ObservableFaces.super.size();
        }

        @Override
        public int get(int index) {
            Face f = ObservableFaces.super.get(index/6);

            if(f.color<0)
                f.color += 0;
            
            return switch (index % 6) {
                case 0 -> f.v0.getIndex();
                case 2 -> f.v1.getIndex();
                case 4 -> f.v2.getIndex();
                // 1 3 5 common color index
                default -> 3 + (f.color)%6;
            };
        }
    };


    @Override
    protected final void fireChange(boolean sizeChanged, int from, int to) {
        values.submitChange(sizeChanged, 6*from, 6*to);
    }

    protected final void fireChange(int i) {
        fireChange(false, i, i+1);
    }

    public void addTarget(ObservableIntegerArray target) {
        values.addTarget(target);
        this.target = target;
    }

    private ObservableIntegerArray target = null;

    @Override
    public boolean verify(Predicate<? super Face> face) {

        assert verifyTarget();

        return super.verify(face);
    }

    private boolean verifyTarget() {
        if(target!=null) {
            int size = values.size();
            if(target.size() != size) {
                assert false : "size invalid";
                return false;
            }


            for(int i=0; i<size; ++i) {
                int i0 = values.get(i);
                int i1 = target.get(i);

                if(i0 != i1) {
                    assert false : "value invalid";
                    return false;
                }
            }
        }

        return true;
    }
}
