package ditz.atrops.hedron.colors;

import ditz.atrops.collections.Indexed;

public class Colored extends Indexed {

    public int color = -1;

    public Colored(int index) {
        super(index);
        this.color = Math.max(index, 0);
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
