package ditz.atrops.collections;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableArrayBase;

abstract public class AbstractObservableArray<T extends ObservableArray<T>> extends ObservableArrayBase<T> {

    public void addAll(T src) {
        throw new UnsupportedOperationException();
    }

    public void addAll(T src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    public void setAll(T src) {
        throw new UnsupportedOperationException();
    }

    public void setAll(T src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    public void set(int destIndex, T src, int srcIndex, int length) {
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

    public void submitChange(boolean sizeChanged, int from, int to) {
        fireChange(sizeChanged, from, to);
    }
}
