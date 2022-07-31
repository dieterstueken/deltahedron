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

    void draw(Vertex v, Coord2DConsumer target) {
        double x = v.p0.getX();
        double y = v.p0.getY();
        double z = v.p0.getZ();

        double r = Math.hypot(x, y);
        double d = 0;
        if(r>0) {
            double th = Math.atan2(r, z);
            d = th / Math.PI / r;
        }
        
        d *= size/2.5;

        target.apply(d*(x+1), d*(y+1));
    }

    public void draw(Geodesic sphere) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        for (Vertex v0 : sphere.points) {
            for(Vertex v1:v0.adjacents) {
                if(v1.getIndex()<v0.getIndex()) {
                    gc.beginPath();
                    draw(v0, gc::moveTo);
                    draw(v1, gc::lineTo);
                    gc.stroke();
                }
            }
        }
    }
}
