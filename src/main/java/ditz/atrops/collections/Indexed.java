package ditz.atrops.collections;

import java.util.Comparator;

public class Indexed {

    public static Comparator<Indexed> ORDER = Comparator.comparingInt(Indexed::getIndex);

    private int index;

    protected Indexed(int index) {
        this.index = index;
    }

    protected Indexed() {
        this(-1);
    }

    public int getIndex() {
        return index;
    }

    public boolean setIndex(int index) {
        boolean wasValid = isValid();
        this.index = index;
        return wasValid;
    }

    public boolean isValid() {
        return index>=0;
    }

    public boolean verify() {
        assert isValid();
        return isValid();
    }

    public boolean invalidate() {
        return setIndex(-1);
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return getName() + "[" + getIndex() + "]";
    }
}
