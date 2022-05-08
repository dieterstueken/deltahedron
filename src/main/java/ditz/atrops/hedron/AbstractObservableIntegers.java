package ditz.atrops.hedron;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableIntegerArray;

public abstract class AbstractObservableIntegers extends ObservableArrayBase<ObservableIntegerArray> implements ObservableIntegerArray {
    void onChanged(ListChangeListener.Change<? extends Face> c) {
        while (c.next()) {
            System.out.print("faces ");
            System.out.println(c);
            /*
            if (c.wasPermutated()) {
                for (int i = c.getFrom(); i < c.getTo(); ++i) {
                    int j = c.getPermutation(i);
                    System.out.print("permuted: ");
                    System.out.print(i);
                    System.out.print(" - ");
                    System.out.print(j);
                    System.out.println();
                }
            } else {
                String what = "changed: ";

                if (c.wasUpdated())
                    what = "updated: ";
                if (c.wasAdded())
                    what = "added: ";
                if (c.wasRemoved())
                    what = "removed: ";

                for (int i = c.getFrom(); i < c.getTo(); ++i) {
                    System.out.print(what);
                    System.out.print(i);
                    System.out.println();
                }
            }
            */
        }
    }

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

    @Override
    public void addAll(int... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(ObservableIntegerArray src) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(int[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(ObservableIntegerArray src, int srcIndex, int length) {
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
    public void setAll(ObservableIntegerArray src) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAll(ObservableIntegerArray src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int destIndex, int[] src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int destIndex, ObservableIntegerArray src, int srcIndex, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int index, int value) {
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
