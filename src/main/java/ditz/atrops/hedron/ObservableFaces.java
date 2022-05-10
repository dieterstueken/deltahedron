package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableIntegers;
import ditz.atrops.collections.ObservableArrayList;
import javafx.collections.ObservableIntegerArray;

public class ObservableFaces extends ObservableArrayList<Face> {

    AbstractObservableIntegers faces = new AbstractObservableIntegers() {

        @Override
        public int size() {
            return 6*ObservableFaces.super.size();
        }

        @Override
        public int get(int index) {
            Face f = ObservableFaces.super.get(index/6);

            return switch (index % 6) {
                case 0 -> f.v0.index;
                case 2 -> f.v1.index;
                case 4 -> f.v2.index;
                default -> 3 + (f.color)%6;
            };
        }
    };

    @Override
    protected final void fireChange(boolean sizeChanged, int from, int to) {
        faces.submitChange(sizeChanged, 6*from, 6*to);
    }

    public void addTarget(ObservableIntegerArray target) {
        faces.addTarget(target);
    }
}
