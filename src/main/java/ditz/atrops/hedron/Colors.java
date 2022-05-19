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

    static double fmod(double f, int m) {
        return (f-m*Math.floor(f/m));
    }

    /**
     * Return a cell index for a 3x3 grid.
     * Each cell is divided into an upper and a lower cell.
     * Each cell is then divided into two triangles.
     *
     *   +---+---+---+
     *   |2/3|4/5|6/7|
     *   |6/7|8/9|0/1|
     *   |0/1|2/3|4/5|
     *   +---+---+---+
     *
     * @param xi coordinate
     * @param yi coordinate
     * @return cell index.
     */

    static int icell(double xi, double yi) {
        xi = fmod(xi, 3);
        yi = fmod(yi, 3);

        int i = (int) xi;
        int j = (int) yi;
        int k = mod(yi-xi, 2);

        int ic = 6*j + 2*i + k%2;

        return ic;
    }

    /**
     * Colored basic cell.
     *
     *  +-----------+-----------+
     *  |0 1 1 1 2 2 2 3 3 3 0 0|
     *  |1 1 1 0 0 0 3 3 3 2 2 2|
     *  |3 3 0 0 0 1 1 1 2 2 2 3|
     *  |3 2 2 2 1 1 1 0 0 0 3 3|
     *  |2 2 2 3 3 3 0 0 0 1 1 1|
     *  |0 0 3 3 3 2 2 2 1 1 1 0|
     *  +-----------+-----------+
     */

    static final char[] ICOLOR = {
            0,0,2,2,2,3,
            3,3,3,2,2,2,
            2,3,3,3,1,1,
    };

    static final List<Color> COLORS = List.of(Color.WHITE, Color.RED, Color.GREEN, Color.BLUE);

    /**
     * Four basic cell form a 12x12 super cell with permuted colors.
     *
     *  |2 |1 |
     *  |0 |3 |
     */

    static int icolor(double xi, double yi) {
        int ic = icell(xi, yi);
        int color = ICOLOR[ic];

        int i = mod(xi,6)/3;
        int j = mod(yi,6)/3;
        int k = 3*(i%2) + 2*(j%2);

        color += k;

        return color & 3;
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

    Color color(double x, double y) {

        double xi = C * (x*A + y*B) / size;
        double yi = C * (x*B + y*A) / size;

        int icol = icolor(xi, yi);

        return COLORS.get(icol);
    }
    
    private final int size;

    private final WritableImage image;

    public Colors(int size) {

        this.size = size;

        size *= 7;
        
        this.image = new WritableImage(size, size);

        PixelWriter pw  = image.getPixelWriter();

        for(int j=0; j<size; ++j) {

            for(int i=0; i<size; ++i) {
                Color color = color(i,j);
                pw.setColor(i, j, color);
            }
        }
    }

    public static void main(String ... args) throws IOException {

        Colors colors = new Colors(500);

        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(colors.image, null), "png", new File("palette.png"));
    }
}
