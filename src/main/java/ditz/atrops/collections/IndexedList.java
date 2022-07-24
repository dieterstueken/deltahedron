package ditz.atrops.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.function.Predicate;

abstract public class IndexedList<T extends Indexed> extends AbstractList<T> {

    protected final ArrayList<T> elements = new ArrayList<>();

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public T get(int index) {
        T element = elements.get(index);

        assert element.verify();

        return element;
    }

    public boolean verify(Predicate<? super T> verify) {

        for (T element : this) {
            if (!verify.test(element))
                return false;
        }

        return true;
    }

    public boolean verify() {
        return verify(T::verify);
    }

    abstract protected void fireChange(boolean sizeChanged, int from, int to);

    public void fireChange() {
        fireChange(false, 0, size());
    }

    protected void sizeChanged() {
        fireChange(true, 0, 0);
    }

    protected void fireChange(int i) {
        fireChange(false, i, i+1);
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

        assert element.verify();

        sizeChanged();

        assert verify();

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

        assert verify();

        // pop off tail element
        int j = elements.size()-1;
        T element = elements.remove(j);

        if (j != index) {
            // the removed element was not the element at index.
            // This this element has to be retained.
            // Push it back to the list to replace the element to delete.
            T replaced = elements.set(index, element);
            setup(element, index);
            update(index);
            element = replaced;
        }
        // propagate size check.
        sizeChanged();

        // invalidate removed element
        invalidate(element);

        assert verify();

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
