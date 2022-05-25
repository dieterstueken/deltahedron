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
public class Tetraeder extends AbstractList<Point3D> implements RandomAccess {

    public static Tetraeder UNIT = new Tetraeder(1.0);

    private final Cube cube;

    public Tetraeder(double radius) {
        this.cube = new Cube(radius);
    }

    public int size() {
        return 4;
    }

    public Point3D get(int i) {
        return switch (i % 4) {
            case 0 -> cube.get(0);
            case 1 -> cube.get(3);
            case 2 -> cube.get(5);
            default -> cube.get(6);
        };
    }
}
