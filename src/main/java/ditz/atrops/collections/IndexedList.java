package ditz.atrops.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.RandomAccess;

abstract public class IndexedList<T extends Indexed> extends AbstractList<T> implements RandomAccess {

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

    protected void sizeChanged() {
        fireChange(true, 0, 0);
    }

    public void update(int index) {
        if(index>=0 && index<size())
            fireChange(false, index, index+1);
    }

    /**
     * Append an element to the list and set up its index.
     * @param element to append.
     * @return true.
     */
    @Override
    public boolean add(T element) {
        int i = elements.size();
        elements.add(element);
        element.setIndex(i);
        sizeChanged();
        return true;
    }

    /**
     * replace some element from the list by another element.
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element replaced.
     */
    @Override
    public T set(int index, T element) {
        element.setIndex(index);
        T replaced = elements.set(index, element);
        replaced.setIndex(-1);
        update(index);
        return replaced;
    }

    /**
     * Remove the element at index and return the removed element.
     *
     * @param index the index of the element to be removed
     * @return the removed element.
     */
    public T remove(int index) {

        // pop off tail element
        int j = elements.size()-1;
        T element = elements.remove(j);
        if (j == index) {
            // this was already the element to remove.
            element.setIndex(-1);
        } else {
            // this element has to be retained.
            // push it back to the list to replace the element to delete.
            element = set(index, element);
        }

        // propagate size check.
        sizeChanged();

        return element;
    }

    @Override
    public int indexOf(Object obj) {
        return obj instanceof Indexed indexed ? indexOf(indexed) : -1;
    }

    /**
     * lookup an Indexed object.
     * Use its index for a fast lookup.
     *
     * @param obj to find.
     * @return its index, or -1.
     */
    public int indexOf(Indexed obj) {
        int index = obj.getIndex();
        if(index>=0 && index < size()) {
            T t = get(index);
            if (t == obj)
                return index;
            else
                obj.getIndex();
        }

        return super.indexOf(obj);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public boolean remove(Object obj) {
        int i = indexOf(obj);
        return i >= 0 && remove(i) != null;
    }

    public void clear() {
        elements.clear();
        fireChange(true, 0, 0);
    }
}