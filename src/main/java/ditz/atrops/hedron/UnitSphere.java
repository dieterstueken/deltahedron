package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 16.04.22
 * Time: 13:45
 */
public class UnitSphere extends Geodesic {

    final double MAX = 1L<<32;

    int addPoints(Collection<? extends Point3D> points) {
        points.forEach(this::addPoint);
        return points.size();
    }

    void addPoints(Supplier<Point3D> points, int count) {
        while(this.points.size()<count)
            addPoint(points.get());
    }

    int generate(Supplier<Point3D> points, int n) {
        assert n>3 : "too few points";

        this.points.clear();
        while(this.points.size()<n)
            addPoint(points.get());

        return faces.size();
    }

    void colorize() {

    }
}
