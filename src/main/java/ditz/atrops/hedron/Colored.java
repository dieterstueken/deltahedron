package ditz.atrops.hedron;

import ditz.atrops.collections.Indexed;

public class Colored extends Indexed {

    int color = -1;

    public Colored(int index) {
        super(index);
    }

    public Colored() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String toString() {
        return getName() + "[" + getIndex() + ':' + getColor() + "]";
    }
}
