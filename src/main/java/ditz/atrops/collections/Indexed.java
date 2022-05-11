package ditz.atrops.collections;

public class Indexed {

    private int index;

    protected Indexed(int index) {
        this.index = index;
    }

    protected Indexed() {
        this(0);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return getName() + "[" + getIndex() + "]";
    }
}
