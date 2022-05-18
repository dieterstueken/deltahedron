package ditz.atrops.hedron;

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
    static int mod(float f, int m) {
        return (int) (f+m*Math.floor(f/m));
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
     * @param x coordinate
     * @param y coordinate
     * @return cell index.
     */

    static int icell(float x, float y) {
        int i = mod(x, 6);
        int j = mod(y, 3);
        return 2*(i%3) + 6*(j%3) + (i-j)&1;
    }

    /**
     * Colored basic cell.
     *
     *  +---+---+---+
     *  |1 1|3 3 3/2|
     *  |1/0 0 0|1 1|
     *  |0 0 0/1 1 1|
     *  +---+---+---+
     */

    static final char[] ICOLOR = {
            1,1,3,3,3,2,
            1,0,0,0,1,1,
            0,0,0,1,1,1};

    /**
     * Four basic cell form a 12x12 super cell with permuted colors.
     *
     *  |2 |1 |
     *  |0 |3 |
     */

    int icolor(float x, float y) {
        int color = ICOLOR[icell(x,y)];

        int i = mod(x,12)/6;
        int j = mod(y,12)/6;
        int k = 3*(i%2) + 2*(j%2);

        color += k;
        color &= 4;

        return color;
    }

    /**
     *
     *  Transformation:
     *
     *  rotation by 15° with:
     *
     *  s = sin(15°) = (√6 - √2)/4
     *  c = cos(15°) = (√6 + √2)/4
     *  t = s/c = 2 + √3
     *
     *  triangle edge from (1,1) -> (5,3) with length 3√2
     *  matches to (x,y): (0.5, 0,5) -> (n-0.5, t*(n-0.5)) with length (n-1)/c
     *
     *  x-0.5                       | c  -s|  i-1
     *         = (n-1) / (c * 3√2)  |      |
     *  y-0.5                       | s  c |  j-1
     *
     * inverted:
     *  i-1                 | c*s  s*s |  x - 0.5
     *         = 3√2/(n-1)  |          |
     *  j-1                 |-s*c  c*c |  y - 0.5
     *
     *      with: c*s = 1/4
     *      and   c*c = (2-√3)/4
     *
     */
}
