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

    //private final Cube cube;

    final Point3D p0 ;
    final Point3D p1;
    final Point3D p2;
    final Point3D p3;

    public Tetraeder(double radius) {
        p0 = new Point3D(0, 0, 1);

        double ct = -1.0/3;
        double st = Math.sqrt(8)/3;
        p1 = new Point3D(st, 0, ct);

        double sct = st * ct;
        double sst = st * st;
        p2 = new Point3D(sct, sst, ct);
        p3 = new Point3D(sct,-sst, ct);
    }

    public int size() {
        return 4;
    }

    public Point3D get(int i) {
        return switch (i % 4) {
            case 0 -> p0;
            case 1 -> p1;
            case 2 -> p2;
            default -> p3;
        };
    }
}
