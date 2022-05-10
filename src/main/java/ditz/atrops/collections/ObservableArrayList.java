package ditz.atrops.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.RandomAccess;

abstract public class ObservableArrayList<T> extends AbstractList<T> implements RandomAccess {

    final ArrayList<T> elements = new ArrayList<>();

    @Override
    public T get(int index) {
        return elements.get(index);
    }

    @Override
    public int size() {
        return elements.size();
    }

    abstract protected void fireChange(boolean sizeChanged, int from, int to);

    public boolean add(T element) {
        int i = elements.size();
        elements.add(element);
        fireChange(true, i, i+1);
        return true;
    }

    public T remove(int index) {
        T removed = elements.get(index);

        int j = elements.size()-1;
        T tmp = elements.remove(j);
        if(j!=index) {
            elements.set(index, tmp);
            fireChange(true, index, index+1);
        } else {
            fireChange(true, 0, 0);
        }

        return removed;
    }

    public boolean remove(Object element) {
        int i = elements.indexOf(element);
        return i >= 0 && remove(i) != null;
    }

    public void clear() {
        elements.clear();
        fireChange(true, 0, 0);
    }
}
