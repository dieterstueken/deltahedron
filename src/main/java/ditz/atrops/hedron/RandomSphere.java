package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public class RandomSphere extends UnitSphere {

    RandomPoints rand = new RandomPoints();

    RandomSphere(int initial) {
        generate(rand::nextPoint, initial);
    }

    public RandomSphere(Collection<? extends Point3D> points) {
        addPoints(points);
    }

    RandomSphere() {
        this(4);
    }

    public void addPoint() {
        addPoint(rand.nextPoint());
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

        int lim = 100;

        for(int i=0; i<lim; ++i) {

            s.addPoints(10);

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
