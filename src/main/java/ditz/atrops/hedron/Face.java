package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  29.04.2022 13:26
 * modified by: $
 * modified on: $
 */
public class Face extends AbstractList<Vertex> implements RandomAccess {

    public static double det(Point3D u, Point3D v, Point3D w) {
        return    u.getX() * (v.getY() * w.getZ() - v.getZ() * w.getY())
                + u.getY() * (v.getZ() * w.getX() - v.getX() * w.getZ())
                + u.getZ() * (v.getX() * w.getY() - v.getY() * w.getX());
    }

    public static Point3D nom(Point3D u, Point3D v, Point3D w) {
        double x = u.getY()*v.getZ() - u.getZ()*v.getY() + v.getY()*w.getZ() - v.getZ()*w.getY() + w.getY()*u.getZ() - w.getZ()*u.getY();
        double y = u.getZ()*v.getX() - u.getX()*v.getZ() + v.getZ()*w.getX() - v.getX()*w.getZ() + w.getZ()*u.getX() - w.getX()*u.getZ();
        double z = u.getX()*v.getY() - u.getY()*v.getX() + v.getX()*w.getY() - v.getY()*w.getX() + w.getX()*u.getY() - w.getY()*u.getX();

        return new Point3D(x,y,z);
    }

    final Vertex v0, v1, v2;

    final Point3D nom;
    final double det;

    public Face(Vertex v0, Vertex v1, Vertex v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;

        this.det = det(v0.p0, v1.p0, v2.p0);
        this.nom = nom(v0.p0, v1.p0, v2.p0);
    }

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

    // dist from face
    public double dist(Point3D p) {
        return nom.dotProduct(p) - det;
    }

    public double dist(Vertex v) {
        return dist(v.p0);
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

    public void connect() {
        this.forEach(this::connectTo);
    }

    public void cutOff() {
        this.forEach(this::removeFrom);
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

    public void addVertex(Vertex vx, Faces reg) {

        double dx = dist(vx);
        if(dx<0)
            throw new IllegalArgumentException("add outside vertex");

        // unregister from own vertices.
        reg.removeFace(this);

        // process adjacent edges / faces.
        for(int j=0; j<3; ++j) {

            Face f = getAdjacent(j);
            if(f==null)
               continue;

            double d = f.dist(vx);

            // face is visible
            if(d>0) {
                f.addVertex(vx, reg);
            } else {

                // invisible or missing face, just add an edge
                Vertex p1 = get(j + 1);
                Vertex p2 = get(j + 2);
                reg.newFace(p1, p2, vx);
            }
        }
    }

    public Face getAdjacent(int i) {
        Vertex p1 = get(i+1);
        Vertex p2 = get(i+2);
        for (Face f : p1.faces) {
            if(f.contains(p2)) {
                return f;
            }
        }

        return null;
    }

    public int furthestTo(Point3D p) {
        double d0 = v0.pyth(p);
        double d1 = v1.pyth(p);
        double d2 = v2.pyth(p);
        if(d0>Math.max(d1, d2))
            return 0;
        if(d1>Math.max(d2, d0))
            return 1;
        return 2;
    }

    public String toString() {
        return String.format("%d:%d:%d:%.2f", v0.index, v1.index, v2.index, det);
    }

    public int hashCode() {
        return v0.index + 31*(v1.index + 31*v2.index);
    }

    public boolean equals(Object o) {
        if(this==o)
            return true;

        if(o instanceof Face of) {

            if(!of.v0.equals(v0))
                return false;

            if(!of.v1.equals(v1))
                return false;

            if(!of.v2.equals(v2))
                return false;

            throw new IllegalStateException("equal faces");
        }

        return false;
    }
}
