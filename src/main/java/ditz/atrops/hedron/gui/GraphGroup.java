package ditz.atrops.hedron.gui;

import ditz.atrops.hedron.Geodesic;
import ditz.atrops.hedron.Vertex;
import ditz.atrops.hedron.colors.Colors;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import static javafx.scene.shape.ArcType.CHORD;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 31.07.22
 * Time: 15:10
 */
public class GraphGroup {

    static double size = 600;
    static double dot = 6;

    final Geodesic sphere;

    ReadOnlyObjectProperty<Transform> transform;

    final Canvas canvas = new Canvas(size,size);

    public GraphGroup(Geodesic sphere, Node objects) {
        this.sphere = sphere;
        this.transform = objects.localToSceneTransformProperty();
        transform.addListener(obs -> draw());
    }

    Point2D transform(double x, double y, double z) {
        double r = Math.hypot(x, y);
        double d = 0;
        if(r>0) {
            double th = Math.atan2(r, z);
            d = th / Math.PI / r;
        } else {
            if(z<0) {
                d = 1;
                x = 1;
            }
        }

        d *= size/2.5;

        return new Point2D(size/2+d*x, size/2-d*y);
    }

    Point2D transform(Point3D p) {
        return transform(p.getX(), p.getY(), p.getZ());
    }

    void draw(GraphicsContext gc, Point3D v0, Point2D p0, Point3D v1, Point2D p1) {
        double d = p0.distance(p1);

        if(d<10) {
            gc.lineTo(p1.getX(), p1.getY());

        } else {
            Point3D vm = v0.midpoint(v1).normalize();
            Point2D pm = transform(vm);
            draw(gc, v0, p0, vm, pm);
            draw(gc, vm, pm, v1, p1);
        }
    }

    void draw(GraphicsContext gc, Point3D v0, Point3D v1) {
        Point2D p0 = transform(v0);
        Point2D p1 = transform(v1);
        gc.beginPath();
        gc.moveTo(p0.getX(), p0.getY());
        draw(gc, v0, p0, v1, p1);
        gc.stroke();
    }

    void draw(GraphicsContext gc, Vertex v0, Vertex v1) {
        Transform t = transform.get();
        Point3D p0 = t.transform(v0.p0);
        Point3D p1 = t.transform(v1.p0);
        draw(gc, p0, p1);
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (Vertex v0 : sphere.points) {
            for(Vertex v1:v0.adjacents) {
                if(v1.getIndex()>v0.getIndex()) {
                    draw(gc, v0, v1);
                }
            }
        }

        Transform t = transform.get();

        for (Vertex v0 : sphere.points) {
            Point2D p0 = transform(t.transform(v0.p0));
            Color color = Colors.COLORS.get(v0.color%4);
            gc.setFill(color);
            gc.fillArc(p0.getX()-dot, p0.getY()-dot, 2*dot, 2*dot, 0, 360, CHORD);
        }
    }
}
