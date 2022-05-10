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

    public Vertex addPoint(Point3D point) {

        double dist = Double.NEGATIVE_INFINITY;
        Face faced = null;

        for (Vertex v : points) {
            if(v.pyth(point)*MAX<1) // reject
                return null;

            for (Face f : v.faces) {
                if(f!=faced) { // already found
                    double d = f.dist(point);
                    if (faced == null || d > dist || (d==dist && f.det>faced.det)) {
                        faced = f;
                        dist = d;
                    }
                }
            }
        }

        if(faced==null) {

            if(points.size()<4) {
                Vertex vx = super.addPoint(point);

                // build flat triangles
                if (points.size() == 3) {
                    Vertex v0 = points.get(0);
                    Vertex v1 = points.get(1);

                    newFace(v0, v1, vx);
                    newFace(v1, v0, vx);
                }
                return vx;
            }
        } else {
            Vertex vx = super.addPoint(point);
            faced.addVertex(vx, this);
            return vx;
        }

        // outside?
        return null;
    }

    void generate(int n) {
        generate(new RandomPoints(), n);
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
        RandomPoints rand = new RandomPoints();

        for(int i=0; i<30; ++i) {
            s.clear();
            s.addPoints(rand, 127);

        }
    }
}
