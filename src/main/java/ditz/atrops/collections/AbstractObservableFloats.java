package ditz.atrops.collections;

import javafx.collections.ObservableFloatArray;

public abstract class AbstractObservableFloats extends AbstractObservableArray<ObservableFloatArray> implements ObservableFloatArray {

    @Override
    public float[] toArray(float[] dest) {
        return toArray(0, dest, size());
    }

    @Override
    public float[] toArray(int srcIndex, float[] dest, int length) {
        if ((dest == null) || (length > dest.length)) {
            dest = new float[length];
        }
        copyTo(srcIndex, dest, 0, length);
        return dest;
    }

    @Override
    public void copyTo(int srcIndex, float[] dest, int destIndex, int length) {
        for(int i=0; i<length; ++i)
            dest[destIndex+i] = get(srcIndex+i);
    }

    @Override
    public void copyTo(int srcIndex, ObservableFloatArray dest, int destIndex, int length) {
        dest.set(destIndex, this, srcIndex, length);
    }

    public void addTarget(ObservableFloatArray target) {
        target.setAll(this);
        this.addListener((source, sizeChanged, from, to) -> updateTarget(target, from, to));
    }

    protected void updateTarget(ObservableFloatArray target, int from, int to) {
        int size = size();
        int len = size - target.size();
        if(len>0)
            target.addAll(this, target.size(), len);
        else if(len!=0) {
            target.ensureCapacity((size|1023)+1);
            target.resize(size);
        }

        copyTo(from, target, from, to-from);
    }

    @Override
    public void addAll(float... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(float[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAll(float... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAll(float[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int destIndex, float[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int index, float value) {
        throw new UnsupportedOperationException();
    }
}