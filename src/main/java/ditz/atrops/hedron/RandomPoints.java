package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 18.04.22
 * Time: 20:20
 */
public class RandomPoints extends Random {

    public RandomPoints() {
        super();
    }

    public RandomPoints(long seed) {
        super(seed);
    }

    public Point3D nextPoint() {
        Point3D point=null;

        while(point==null) {
            double x = nextGaussian();
            double y = nextGaussian();

            // avoid poles
            if(x==0 && y==0)
                continue;

            double z = nextGaussian();

            double r = Math.sqrt(x * x + y * y + z * z);
            point = new Point3D(x/r,y/r,z/r);
        }
        return point;
    }

}
