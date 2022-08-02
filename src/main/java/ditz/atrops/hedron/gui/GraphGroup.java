package ditz.atrops.hedron.gui;

import ditz.atrops.hedron.Geodesic;
import ditz.atrops.hedron.Vertex;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 31.07.22
 * Time: 15:10
 */
public class GraphGroup {

    static double size = 600;

    final Geodesic sphere;

    ReadOnlyObjectProperty<Transform> transform;

    //final BorderPane pane;

    final Canvas canvas = new Canvas(size,size);

    public GraphGroup(Geodesic sphere, Node objects) {
        this.sphere = sphere;
        this.transform = objects.localToSceneTransformProperty();
        transform.addListener(obs -> draw());
    }

    void draw(Point3D p0, Point3D p1, double g, Coord2DConsumer target) {
        double h = 1-g;
        double x = g*p0.getX() + h*p1.getX();
        double y = g*p0.getY() + h*p1.getY();
        double z = g*p0.getZ() + h*p1.getZ();

        double r = Math.hypot(x, y);
        double d = 0;
        if(r>0) {
            double th = Math.atan2(r, z);
            d = th / Math.PI / r;
        }
        
        d *= size/2.5;

        target.apply(d*x+size/2, d*y+size/2);
    }

    void draw(GraphicsContext gc, Point3D p0, Point3D p1) {
        Transform t = transform.get();
        p0 = t.transform(p0);
        p1 = t.transform(p1);
        gc.beginPath();
        draw(p0, p1, 0, gc::moveTo);
        for(int i=1; i<50; ++i) {
            draw(p0, p1, i/50.0, gc::lineTo);
        }
        draw(p0, p1, 1, gc::lineTo);
        gc.stroke();
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (Vertex v0 : sphere.points) {
            for(Vertex v1:v0.adjacents) {
                if(v1.getIndex()<v0.getIndex()) {
                    draw(gc, v0.p0, v1.p0);
                }
            }
        }
    }
}
