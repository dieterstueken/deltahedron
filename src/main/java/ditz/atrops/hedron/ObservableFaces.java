package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableIntegers;
import ditz.atrops.collections.IndexedList;
import javafx.collections.ObservableIntegerArray;

public class ObservableFaces extends IndexedList<Face> {

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
}
