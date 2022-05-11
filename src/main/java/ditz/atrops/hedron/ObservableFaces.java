package ditz.atrops.hedron;

import ditz.atrops.collections.AbstractObservableIntegers;
import ditz.atrops.collections.ObservableArrayList;
import javafx.collections.ObservableIntegerArray;

public class ObservableFaces extends ObservableArrayList<Face> implements Faces {

    AbstractObservableIntegers values = new AbstractObservableIntegers() {

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

    int id = 0;

    public Face newFace(Vertex v0, Vertex v1, Vertex v2) {
        Face face = new Face(++id, v0, v1, v2);

        add(face);
        face.connect();

        return face;
    }

    @Override
    public Face addFace(Face face) {
        boolean added = super.add(face);
        if(!added)
            throw new IllegalStateException("face already registered");
        face.connect();
        return face;
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
}
