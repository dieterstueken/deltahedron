package ditz.atrops.hedron.gui;

import ditz.atrops.hedron.Geodesic;
import ditz.atrops.hedron.Vertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 31.07.22
 * Time: 15:10
 */
public class GraphGroup {

    static double size = 600;

    //final BorderPane pane;

    final Canvas canvas = new Canvas(size,size);

    public GraphGroup() {
        //pane = new BorderPane(canvas);
    }

    void draw(Vertex v1, Vertex v2, double g, Coord2DConsumer target) {
        double h = 1-g;
        double x = g*v1.p0.getX() + h*v2.p0.getX();
        double y = g*v1.p0.getY() + h*v2.p0.getY();
        double z = g*v1.p0.getZ() + h*v2.p0.getZ();

        double r = Math.hypot(x, y);
        double d = 0;
        if(r>0) {
            double th = Math.atan2(r, z);
            d = th / Math.PI / r;
        }
        
        d *= size/2.5;

        target.apply(d*x+size/2, d*y+size/2);
    }

    void draw(GraphicsContext gc, Vertex v0, Vertex v1) {
        gc.beginPath();
        draw(v0, v1, 0, gc::moveTo);
        for(int i=1; i<50; ++i) {
            draw(v0, v1, i/50.0, gc::lineTo);
        }
        draw(v0, v1, 1, gc::lineTo);
        gc.stroke();
    }

    public void draw(Geodesic sphere) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (Vertex v0 : sphere.points) {
            for(Vertex v1:v0.adjacents) {
                if(v1.getIndex()<v0.getIndex()) {
                    draw(gc, v0, v1);
                }
            }
        }
    }
}
