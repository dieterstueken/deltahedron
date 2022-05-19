package ditz.atrops.hedron;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 18.05.22
 * Time: 07:52
 */
public class Colors {

    /**
     * Modulo function to map a float to a periodic int.
     * @param f coordinate to divide.
     * @param m modulo periode.
     * @return modulo index.
     */
    static int mod(double f, int m) {
        return (int) (f-m*Math.floor(f/m));
    }

    /**
     * Colored basic cell.
     */
     
     static final short[] CELL = {
             1,1,0,0,0,2,2,2,3,3,3,1,
             2,2,2,0,0,0,1,1,1,3,3,3,
             0,2,2,2,3,3,3,1,1,1,0,0,
             0,0,1,1,1,3,3,3,2,2,2,0,
             3,3,3,1,1,1,0,0,0,2,2,2,
             1,3,3,3,2,2,2,0,0,0,1,1
    };
     
     /**
     * @param xi coordinate
     * @param yi coordinate
     * @return color index.
     */

    static int icell(double xi, double yi) {
        int j = mod(yi, 6);
        int i = mod(2*xi, 12)/2;
        int k = mod(yi-xi, 2) + i/3 + j/3;

        int ic = 12*j + 2*i + k%2;
        return CELL[ic];
    }

    /**
     *
     *  Transformation:
     *
     *  share by 15° with:
     *
     *  t = tan(15°) = 2 - √3
     *
     *
     *  x               | 2t-1 2-t |  i
     *         = n / 6  |          |
     *  y               | 2-t 2t-1 |  j
     *
     * inverted:
     *  i                  |  2-t  1-2t |  x
     *       = 2/(1-t^2)/n |           |
     *  j                  | 1-2t  2-t |  y
     *
     */


    static final double T, A , B, C;

    static {
        T = 2 - Math.sqrt(3);
        A = 2- T;
        B = 1-2* T;
        C = 2/(1- T * T);
    }

    int icol(double x, double y) {

        double xi = C * (x*A + y*B) / size;
        double yi = C * (x*B + y*A) / size;

        return icell(xi, yi);
    }

    int debug(double x, double y) {
       
        double g = 6.0/size;

        return icell( g*x, g*y);
    }

    static final List<Color> COLORS = List.of(Color.YELLOW, Color.RED, Color.GREEN, Color.BLUE);

    private final int size;

    private final WritableImage image;

    public Colors(int size) {

        this.size = size;

        size += 1;
        
        this.image = new WritableImage(size, size);

        PixelWriter pw  = image.getPixelWriter();

        for(int j=0; j<size; ++j) {
            for(int i=0; i<size; ++i) {
                int icol = icol(i, j);
                Color color = COLORS.get(icol);
                pw.setColor(i, j, color);
            }
        }
    }

    public static void main(String ... args) throws IOException {

        Colors colors = new Colors(128);

        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(colors.image, null), "png", new File("palette.png"));
    }
}
