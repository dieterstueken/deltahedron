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

    int generate(Supplier<Point3D> points, int n) {
        assert n>3 : "too few points";

        this.points.clear();
        while(this.points.size()<n)
            addPoint(points.get());

        return faces.size();
    }

    public static void main(String ... args) {

        UnitSphere s = new UnitSphere();
        s.addPoints(Cube.UNIT);

        s.stat().showLine();

        //s.addPoint(1,0,0);
        //s.addPoint(0,1,0);
        //s.addPoint(0,0,1);
        //
        //s.addPoint(-1,0,0);
        //s.addPoint(0,-1,0);
        //s.addPoint(0,0,-1);
        //
        //s.stat().showLine();

        while(s.points.size()>3) {
            int i = 17%s.points.size();
            s.removePoint(i);
            System.out.format("%-2d: ", i);
            s.stat().showLine();
        }

    }
}
