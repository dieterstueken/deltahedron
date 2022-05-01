package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 18.04.22
 * Time: 20:20
 */
public class RandomPoints implements Supplier<Point3D> {

    final Random rand;

    public RandomPoints() {
        rand = new Random();
    }

    public RandomPoints(long seed) {
        rand = new Random(seed);
    }

    public Point3D get() {
        Point3D point=null;

        while(point==null) {
            double x = rand.nextGaussian();
            double y = rand.nextGaussian();

            // avoid poles
            if(x==0 && y==0)
                continue;

            double z = rand.nextGaussian();

            double r = Math.sqrt(x * x + y * y + z * z);
            point = new Point3D(x/r,y/r,z/r);
        }
        return point;
    }

}
