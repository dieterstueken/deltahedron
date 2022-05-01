package ditz.atrops.hedron;

import javafx.geometry.Point3D;


import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  30.04.2022 15:03
 * modified by: $
 * modified on: $
 */
public class Cube extends AbstractList<Point3D> implements RandomAccess {

    public static Cube UNIT = new Cube(1.0);

    private final double d;

    public Cube(double radius) {
        this.d = Math.sqrt(radius/3);
    }

    public int size() {
        return 8;
    }

    public Point3D get(int i) {
        double x = (i&1)==0 ? -d : d;
        double y = (i&2)==0 ? -d : d;
        double z = (i&4)==0 ? -d : d;
        return new Point3D(x,y,z);
    }
}
