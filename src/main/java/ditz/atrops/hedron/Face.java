package ditz.atrops.hedron;

import ditz.atrops.collections.Indexed;
import javafx.geometry.Point3D;

import java.util.AbstractList;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  29.04.2022 13:26
 * modified by: $
 * modified on: $
 */
public class Face extends Indexed {

    public static double det(Point3D u, Point3D v, Point3D w) {
        return    u.getX() * (v.getY() * w.getZ() - v.getZ() * w.getY())
                + u.getY() * (v.getZ() * w.getX() - v.getX() * w.getZ())
                + u.getZ() * (v.getX() * w.getY() - v.getY() * w.getX());
    }

    public static Point3D nom(Point3D u, Point3D v, Point3D w) {
        // u x v + v x w + w x u
        double x = u.getY()*v.getZ() - u.getZ()*v.getY() + v.getY()*w.getZ() - v.getZ()*w.getY() + w.getY()*u.getZ() - w.getZ()*u.getY();
        double y = u.getZ()*v.getX() - u.getX()*v.getZ() + v.getZ()*w.getX() - v.getX()*w.getZ() + w.getZ()*u.getX() - w.getX()*u.getZ();
        double z = u.getX()*v.getY() - u.getY()*v.getX() + v.getX()*w.getY() - v.getY()*w.getX() + w.getX()*u.getY() - w.getY()*u.getX();

        return new Point3D(x,y,z);
    }

    public class Points extends AbstractList<Vertex> implements RandomAccess {

        public int size() {
            return 3;
        }

        public Vertex get(int i) {

            return switch (i % 3) {
                case 2, -2 -> v2;
                case 1, -1 -> v1;
                default -> v0;
            };

        }

        public int indexOf(Object o) {
            if(o==v0)
                return 0;

            if(o==v1)
                return 1;

            if(o==v2)
                return 2;

            return -1;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o)>=0;
        }

        @Override
        public void forEach(Consumer<? super Vertex> action) {
            action.accept(v0);
            action.accept(v1);
            action.accept(v2);
        }

        @Override
        public Spliterator<Vertex> spliterator() {
            return super.spliterator();
        }
    }

    final Vertex v0, v1, v2;

    final Points points = new Points();

    final Point3D nom;
    final double det;

    public int color;

    public Face(int index, Vertex v0, Vertex v1, Vertex v2) {
        super(index);

        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;

        this.det = det(v0.p0, v1.p0, v2.p0);
        this.nom = nom(v0.p0, v1.p0, v2.p0);

        this.color = index;
    }

    public int indexOf(Vertex v) {
        int i = points.indexOf(v);
        if(i<0)
            throw new IllegalArgumentException("vertex not found");
        return i;
    }

    public Vertex getPoint(int index) {
        return points.get(index);
    }

    // dist from face
    public double dist(Point3D p) {
        return nom.dotProduct(p) - det;
    }

    public double dist(Vertex v) {
        return dist(v.p0);
    }

    public void connect() {
        points.forEach(this::connectTo);
    }

    public void cutOff() {
        points.forEach(this::removeFrom);
    }

    private void connectTo(Vertex vx) {
        boolean connected = vx.addFace(this);
        if(!connected)
            throw new IllegalArgumentException("connection failed");
    }

    private void removeFrom(Vertex vx) {
        boolean removed = vx.removeFace(this);
        if(!removed)
            throw new IllegalArgumentException("not on vertex");
    }

    public Face getAdjacent(int i) {
        Vertex p1 = points.get(i+1);
        Vertex p2 = points.get(i+2);
        for (Face f : p1.faces) {
            if(f.points.contains(p2)) {
                return f;
            }
        }

        return null;
    }

    public int furthestTo(Point3D p) {
        double d0 = v0.squareDist(p);
        double d1 = v1.squareDist(p);
        double d2 = v2.squareDist(p);
        if(d0>Math.max(d1, d2))
            return 0;
        if(d1>Math.max(d2, d0))
            return 1;
        return 2;
    }

    public String toString() {
        return String.format("%s (%d:%d:%d)%+.2f", super.toString(), v0.getIndex(), v1.getIndex(), v2.getIndex(), det);
    }

    public int hashCode() {
        return v0.getIndex() + 31*(v1.getIndex() + 31*v2.getIndex());
    }

    public boolean equals(Object o) {
        if(this==o)
            return true;

        if(o instanceof Face of) {

            int i0 = of.points.indexOf(v0);
            if(i0<0)
                return false;

            Vertex v = of.points.get(++i0);
            if(v1.equals(v))
                throw new IllegalStateException("equal edges");

            /*
            v = of.points.get(++i0);
            if(!v2.equals(v))
                return false;

            throw new IllegalStateException("equal faces");
             */
        }

        return false;
    }
}
