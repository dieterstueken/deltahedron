package ditz.atrops.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.RandomAccess;

abstract public class IndexedList<T extends Indexed> extends AbstractList<T> implements RandomAccess {

    protected final ArrayList<T> elements = new ArrayList<>();

    @Override
    public T get(int index) {
        T element = elements.get(index);

        assert index == element.getIndex() : "invalid element index";

        return element;
    }

    @Override
    public int size() {
        return elements.size();
    }

    public void verify() {
        forEach(Indexed::isValid);
    }

    abstract protected void fireChange(boolean sizeChanged, int from, int to);

    protected void sizeChanged() {
        fireChange(true, 0, 0);
    }

    public void update(int index) {
        if(index>=0 && index<size())
            fireChange(false, index, index+1);
    }

    public void update(T element) {
        update(element.getIndex());
    }

    protected boolean invalidate(T element) {
        return element.invalidate();
    }

    /**
     * Setup the new index of element.
     *
     * @param element to index.
     * @param index to set.
     * @return if the setup changed the state to valid.
     * @throws IllegalArgumentException if the new index is negative.
     */
    protected boolean setup(T element, int index) {
        if(index<0)
            throw new IllegalArgumentException("invalid new index");

        return !element.setIndex(index);
    }

    /**
     * Append an element to the list and set up its index.
     * The element index is updated and a notification is sent.
     *
     * @param element to append.
     * @return true.
     */
    @Override
    public boolean add(T element) {

        int index = elements.size();

        elements.add(element);
        setup(element, index);

        sizeChanged();
        return true;
    }

    /**
     * Remove the element at index and return the removed element.
     * The element at index is replaced by the last element from the list.
     * This avoids moving other elements around.
     *
     * @param index the index of the element to be removed
     * @return the removed element.
     */
    public T remove(final int index) {

        // pop off tail element
        int j = elements.size()-1;
        T element = elements.remove(j);

        if (j != index) {
            // the removed element was not the element at index.
            // This this element has to be retained.
            // Push it back to the list to replace the element to delete.
            T replaced = elements.set(index, element);
            update(index);
            setup(element, index);
            element = replaced;
        }

        // invalidate removed element
        invalidate(element);

        verify();

        // propagate size check.
        sizeChanged();

        return element;
    }

    /**
     * Remove some object from the list.
     * Delegate to remove(index).
     *
     * @param obj element to be removed from this list, if present
     * @return if the element was removed.
     */
    public boolean remove(Object obj) {
        int i = indexOf(obj);
        return i >= 0 && remove(i) != null;
    }

    /**
     * Find the index of some object.
     * This delegates to the typed index lookp.
     *
     * @param obj element to search for
     * @return index of the object or -1.
     */
    @Override
    public int indexOf(Object obj) {
        return obj instanceof Indexed indexed ? indexOf(indexed) : -1;
    }

    /**
     * Delegate to indexOf() since elements are unique.
     * @param obj element to search for.
     * @return index of the object or -1.
     */
    @Override
    public int lastIndexOf(Object obj) {
        return indexOf(obj);
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
        }

        // assume this object can not be a member.
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public void clear() {
        elements.clear();
        fireChange(true, 0, 0);
    }
}
