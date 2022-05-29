package ditz.atrops.hedron;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

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
        return (int) fmod(f, m);
    }

    static double fmod(double f, int m) {
        return (f-m*Math.floor(f/m));
    }

    static final List<Color> COLORS = List.of(
            Color.YELLOW,
            Color.PALEVIOLETRED,
            Color.FORESTGREEN,
            Color.AQUA);

    /**
     * Colored 12x6 basic cell.
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

    static double edist(double f, int n) {
        double d = fmod((6*f+n)/3, 2)-1;
        return d*d;
    }


    // line width
    static final int D = 3*128;

    static boolean edge(double xi, double yi) {
        return  D*edist(xi, 1)<1
             || D*edist(yi, -1)<1
             || D*edist(yi-xi, 1)<1;
    }

    static boolean dot(double xi, double yi) {
        double dx = edist(xi, 1);
        double dy = edist(yi, -1);
        double dz = edist(yi-xi, 1);
        return 3*(dx+dy+dz) < 1;
    }

    /**
     *
     *  Transformation:
     *
     *  sheare by 15° with:
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
        A = 2 - T;
        B = 1 - 2*T;
        //C = 2/(1- T * T);
        C = 2/(3*T);
    }

    int icol(double x, double y) {

        double xi = C * (x*A + y*B) / size;
        double yi = C * (x*B + y*A) / size;

        //xi -= 1.0/6;
        //xi -= 1.6*A/12;
        //yi -= 1.6*B/12;

        int icol = icell(xi, yi);

        if(edge(xi, yi))
            icol |= 4;

        if(dot(xi, yi))
            icol |= 8;

        return icol;
    }

    Color color(double x, double y) {
        int icol = icol(x, y);
        
        if((icol&8)!=0)
            return COLORS.get(icol%4);

        if((icol&4)!=0)
            return Color.BLACK;

        return Color.WHITE;
    }

    final int size;

    final WritableImage image;

    final float[] coords;

    public Colors(int size) {

        this.size = size;

        this.coords = coords(size);

        this.image = new WritableImage(size, size);
        PixelWriter pw  = image.getPixelWriter();

        for(int j=0; j<size; ++j) {
            for(int i=0; i<size; ++i) {
                Color color = color(i, j);
                pw.setColor(i, j, color);
            }
        }
    }

    /**
     *    x0    x1  x2
     * x0 0---------+ x0
     *    |     5   | y0
     * x1 | 3       2 y1
     *    |      1  | y2
     * x2 +---4-----+ x2
     *
     * @return array of coords
     */
    private static float[] coords(int size) {

        float x0 = 0.5F/size;
        float x1 = 0.5F;
        float x2 = (size-0.5F)/size;

        float t = (float) T;
        float y0 = t/2 + x0;
        float y1 = t + x0;
        float y2 = (1+t)/2+ x0;

        float[] coords = {
                x0, x0, // R: 0 : 0
                y2, y2, // B: 1 : 1
                x2, y1, // R: 2 : 4
                y0, x1, // G: 3 : 5
                y1, x2, // R: 4 : 2
                x1, y0};// Y: 5 : 3

        return coords;
    }

    public int getTex(Face face, int index) {
        int m = 1<<(face.points.get(index).color%4);

        switch(m) {
            case 1: return 5;
            case 4: return 3;
            case 8: return 1;
        }

        // m==2 red: other colors are relevant to choose either 0, 2 or 4.
        m = 1<<(face.points.get(index+1).color%4);
        m|= 1<<(face.points.get(index+2).color%4);

        return switch (m) {
            case 4, 5 -> 0;
            case 1, 9 -> 2;
            case 8, 12 -> 4;
            default -> 0;
        };
    }

    PhongMaterial getMaterial() {
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseMap(image);
        return mat;
    }

    public static void main(String ... args) throws IOException {

        Colors colors = new Colors(600);

        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(colors.image, null), "png", new File("palette.png"));
    }
}
