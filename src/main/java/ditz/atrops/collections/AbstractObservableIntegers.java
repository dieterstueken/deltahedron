package ditz.atrops.collections;

import javafx.collections.ObservableIntegerArray;

public abstract class AbstractObservableIntegers extends AbstractObservableArray<ObservableIntegerArray> implements ObservableIntegerArray {

    @Override
    public int[] toArray(int[] dest) {
        return toArray(0, dest, size());
    }

    @Override
    public int[] toArray(int srcIndex, int[] dest, int length) {
        if ((dest == null) || (length > dest.length)) {
            dest = new int[length];
        }
        copyTo(srcIndex, dest, 0, length);
        return dest;
    }

    @Override
    public void copyTo(int srcIndex, int[] dest, int destIndex, int length) {
        for (int i = 0; i < length; ++i)
            dest[destIndex + i] = get(srcIndex + i);
    }

    @Override
    public void copyTo(int srcIndex, ObservableIntegerArray dest, int destIndex, int length) {
        dest.set(destIndex, this, srcIndex, length);
    }

    public void addTarget(ObservableIntegerArray target) {
        target.setAll(this);
        this.addListener((source, sizeChanged, from, to) -> updateTarget(target, from, to));
    }

    protected void updateTarget(ObservableIntegerArray target, int from, int to) {
        int size = size();
        int len = size - target.size();
        if(len>0)
            target.addAll(this, target.size(), len);
        else if(len!=0)
            target.resize(size);

        len = to-from;
        if(len>0)
            copyTo(from, target, from, len);
    }

    @Override
    public void addAll(int... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(int[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAll(int... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAll(int[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int destIndex, int[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int index, int value) {
        throw new UnsupportedOperationException();
    }
}
