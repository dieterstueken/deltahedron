package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.*;
import java.util.function.Predicate;

public class RandomSphere extends UnitSphere {

    RandomPoints rand = new RandomPoints();

    List<Point3D> removed = new ArrayList<>();

    @Override
    public Vertex removePoint(int index) {
        Vertex v = super.removePoint(index);
        removed.add(v.p0);
        return v;
    }

    RandomSphere(int initial) {
        generate(rand::nextPoint, initial);
    }

    public RandomSphere(Collection<? extends Point3D> points) {
        addPoints(points);
    }

    RandomSphere() {
        this(4);
    }

    private Point3D nextPoint() {
        if(removed.isEmpty())
            return rand.nextPoint();

        int n = removed.size();
        return removed.remove(n-1);
    }

    public void addPoint() {
        addPoint(nextPoint());
    }

    public void addPoints(int limit) {
        while(points.size()<limit)
            addPoint();
    }

    public void random(int min, int max, int step) {

        int n = points.size();
        int j = rand.nextInt(2*step+1) - step;
        if(j==0)
            j = 1;

        if(n+j < min)
            j = Math.abs(j);

        if(n+j > max)
            j = -Math.abs(j);

        for(int i=0; i<j; i++) {
            addPoint(rand.nextPoint());
        }

        for(int i=0; i<-j; i++) {
            n = points.size();
            n = rand.nextInt(n);
            removePoint(n);
        }
    }

    Vertex findFirst(Predicate<? super Vertex> cond) {
        for (Vertex vertex : points) {
            if(cond.test(vertex)) {
                return vertex;
            }
        }

        return null;
    }

    public static void main(String ... args) {

        RandomSphere s = new RandomSphere();

        Set<Stat> stats = new TreeSet<>();

        int lim = 10000;

        for(int i=0; i<lim; ++i) {

            s.addPoints(12);

            //while(s.points.size()>4) {
            //    Vertex vx = s.findFirst(v->v.faces.size()==3);
            //    if(vx==null)
            //        break;
            //
            //    s.removePoint(vx);
            //}

            Stat stat = s.stat();
            if(stats.add(stat)) {
                lim += 3*i;
                System.out.format("size %d: %d %d %3.1f\n", stats.size(), i, lim, 100.0*i/lim);
                stat.showLine();
            }

            s.clear();
        }

        System.out.format("size: %d\n", stats.size());

        for (Stat stat : stats) {
            stat.showLine();
        }
    }
}
