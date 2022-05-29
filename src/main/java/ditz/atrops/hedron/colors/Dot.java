package ditz.atrops.hedron.colors;

import ditz.atrops.collections.Indexed;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 29.05.22
 * Time: 23:58
 */
class Dot extends Indexed {

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

    static final double G = T/(1-T*T);

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

    static double xc(double xi, double yi) {
        double g = A*(xi-7) - B*(yi-8);
        return (G*g + 3)/6;
    }

    static double yc(double xi, double yi) {
        double g = A*(yi-8) - B*(xi-7);
        return (G*g + 3)/6;
    }

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
        double d = Dot.fmod((6*f+n)/3, 2)-1;
        return d*d;
    }

    // line width
    static final int D = 3*512;

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
        double xi = (C * (x * A + y * B) + 7) / 3;
        double yi = (C * (x * B + y * A) + 8) / 3;

        int icol = icell(xi, yi);

        if(edge(xi, yi))
            icol |= 4;

        if(dot(xi, yi))
            icol |= 8;

        return icol;
    }

    public final byte i, j, icol;

    public final float x, y;

    Dot(int i, int j) {
        this.i = (byte) i;
        this.j = (byte) j;
        this.icol = (byte) icell(this.i / 3.0, this.j / 3.0);

        this.x = (float) xc(i, j);
        this.y = (float) yc(i, j);
    }

    @Override
    public String toString() {
        return String.format("%d:%02X.%c", getIndex(), 16*i+j, "YRGB".charAt(icol));
    }
}
