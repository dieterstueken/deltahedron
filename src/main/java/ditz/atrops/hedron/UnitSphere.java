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

    int addPoints(Collection<Point3D> points) {
        points.forEach(this::addPoint);
        return points.size();
    }

    void addPoints(Supplier<Point3D> points, int count) {
        while(this.points.size()<count)
            addPoint(points.get());
    }

    void generate(int n) {
        generate(new RandomPoints()::nextPoint, n);
    }

    int generate(Supplier<Point3D> points, int n) {
        assert n>3 : "too few points";

        this.points.clear();
        while(this.points.size()<n)
            addPoint(points.get());

        return faces.size();
    }

    public static void main(String ... args) {

        RandomSphere s = new RandomSphere(50);

        for(int i=0; i<30; ++i) {
            s.random(30, 50, 10);
            s.stat().showLine();
        }
    }
}
