package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 16.04.22
 * Time: 13:45
 */
public class UnitSphere implements Faces {

    final double MAX = 1L<<32;

    final List<Vertex> points = new ArrayList<>();

    final Set<Face> faces = new HashSet<>();

    @Override
    public Face newFace(Vertex v0, Vertex v1, Vertex v2) {
        Face face = new Face(v0, v1, v2);
        addFace(face);

        return face;
    }

    @Override
    public void addFace(Face face) {
        boolean added = faces.add(face);
        if(!added)
            throw new IllegalStateException("face already registered");
        face.connect();
    }

    @Override
    public void removeFace(Face face) {
        boolean removed = faces.remove(face);
        face.cutOff();

        if(!removed)
            throw new IllegalStateException("face not registered");
    }

    int addPoints(Collection<Point3D> points) {
        points.forEach(this::addPoint);
        return points.size();
    }

    void addPoints(Supplier<Point3D> points, int count) {
        while(this.points.size()<count)
            addPoint(points.get());
    }

    boolean addPoint(Point3D point) {

        double dist = Double.NEGATIVE_INFINITY;
        Face faced = null;

        for (Vertex v : points) {
            if(v.pyth(point)*MAX<1)
                return false;

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
                Vertex vx = newVertex(point);

                // build flat triangles
                if (points.size() == 3) {
                    Vertex v0 = points.get(0);
                    Vertex v1 = points.get(1);

                    newFace(v0, v1, vx);
                    newFace(v1, v0, vx);
                }
                return true;
            }
        } else {
            Vertex vx = newVertex(point);
            faced.addVertex(vx, this);
            return true;
        }

        // outside?
        return false;
    }

    private Vertex newVertex(Point3D point) {
        int i = points.size();
        Vertex v = new Vertex(i, point);
        points.add(v);
        return v;
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

        try {
            RandomPoints rand = new RandomPoints();

            s.addPoints(rand, 8);
            
        } finally {
            System.out.format("points: %d, faces: %d\n", s.points.size(), s.faces.size());

            Stat stat = new Stat();

            for (Vertex v : s.points) {
                stat.add(v.faces.size());
            }

            stat.show();

        }
    }
}
