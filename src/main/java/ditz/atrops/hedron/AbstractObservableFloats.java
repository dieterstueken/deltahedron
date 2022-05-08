package ditz.atrops.hedron;

import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableFloatArray;

public abstract class AbstractObservableFloats extends ObservableArrayBase<ObservableFloatArray> implements ObservableFloatArray {

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

    @Override
    public void addAll(float... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(ObservableFloatArray src) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(float[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(ObservableFloatArray src, int srcIndex, int length) {
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
    public void setAll(ObservableFloatArray src) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAll(ObservableFloatArray src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int destIndex, float[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int destIndex, ObservableFloatArray src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int index, float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resize(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void ensureCapacity(int capacity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void trimToSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
