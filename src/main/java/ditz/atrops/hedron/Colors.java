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
        return 3*(dx+dy+dz) < 2;
    }

    /**
     *
     *  Transformation:
     *
     *  sheare by 15° with:
     *
     *  t = tan(15°) = 2 - √3
     *
     */

    static final double T, A , B, C;

    static {
        T = 2 - Math.sqrt(3);
        A = 2 - T;
        B = 1 - 2*T;
        //C = 2/(1- T * T);
        C = 2/T;  // 3C
    }

    /**
     * input coords within range [0,1].
     *
     * (0.5, 0.5) -> (7/3, 8/3)
     *
     * @param x coord
     * @param y coord
     * @return color mask
     */
    static int icol(double x, double y) {
        // shift to center
        x -= 0.5;
        y -= 0.5;

        // center maps to 7/3, 8/3
        double xi = (C * (x*A + y*B) + 7)/3;
        double yi = (C * (x*B + y*A) + 8)/3;

        int icol = icell(xi, yi);

        if(edge(xi, yi))
            icol |= 4;

        if(dot(xi, yi))
            icol |= 8;

        return icol;
    }

    Color color(int ix, int iy) {
        int icol = icol((ix+0.5)/size, (iy+0.5)/size);

        if((icol&4)!=0)
            return Color.BLACK;

        if((icol&8)!=0)
            return COLORS.get(icol%4);

        return Color.WHITE;
    }

    final int size;

    final WritableImage image;

    final float[] coords;

    public Colors(int size) {

        this.size = size;

        ++size;

        this.coords = coords(size);

        this.image = new WritableImage(size, size);
        PixelWriter pw  = image.getPixelWriter();

        //color(this.size/2.0, this.size/2.0);

        for(int j=0; j<size; ++j) {
            for(int i=0; i<size; ++i) {
                Color color = color(i, j);
                pw.setColor(i, j, color);
            }
        }
    }

    static final double G = 2*T/(1-T*T);

    /**
     * Return the x coordinate for a grid coordinate(i,j).
     * The y coordinate is just the mirror with swapped (y,x)
     * @param xi grid coordinate
     * @param yi grid coordinate
     * @return x coordiante for (i, j);
     */
    static double xcoord(double xi, double yi) {

        xi -= 7;
        yi -= 8;

        double x = G * (A*xi - B*yi) + 0.5;
        //double y = G * (A*yi - B*xi) + 0.5;

        return x;
    }

    private static byte[] ICOR = {
                1,2, 1,4, 1,7,
              1,5, 4,5, 7,5, 10,5,
            1,8, 4,8, 7,8, 10,8, 13,8,
             4,11, 7,11, 10,11, 13,11,
                4,14, 7,14, 19,14
    };

    private static short[] triplets = {
            0,4,1, // GYR

            3,4,0, // GGY
            4,5,1, // GYY
            1,5,2, // YYY
            5,6,2, // YRY

            3,4,8, // GGG
            4,5,9, // GYB
            5,6,10, // YRB



    }

    /**
     * Create 19 coordinate pairs.
     * @return texture coordinates.
     */
    private static float[] coords() {
        int n = ICOR.length;
        float[] coords = new float[n];
        for(int k=0; k<n; k += 2) {
            int i = ICOR[k];
            int j = ICOR[k+1];
            coords[i] = (float) xcoord(i, j);
            coords[i+1] = (float) xcoord(j, i);
        }

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
