package ditz.atrops.hedron.colors;

import ditz.atrops.hedron.Face;
import ditz.atrops.hedron.Geodesic;
import ditz.atrops.hedron.Vertex;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 03.07.22
 * Time: 19:32
 */
public class Dyer {

    final Geodesic sphere;

    final ArrayList<Vertex> points = new ArrayList<>();

    int seq = 0;

    public Dyer(Geodesic sphere) {
        this.sphere = sphere;
    }

    private int setup() {
        // reset all color masks.
        sphere.points.forEach(Dyer::resetColor);
        points.clear();

        if(sphere.faces.isEmpty())
            return 0;

        points.ensureCapacity(sphere.points.size());

        Face.Points f0 = sphere.faces.get(0).points;
        for (int i = 0; i < f0.size(); i++) {
            Vertex vx = f0.get(i);
            setColor(vx, i+seq);
            points.add(vx);
        }

        int n = 0;

        for (Vertex vx : f0) {
            for (Vertex adj : vx.adjacents) {
                n += setup(adj);
            }
        }

        for (int i = 3; i < points.size(); i++) {
            deColor(points.get(i));
        }

        ++seq;

        return n;
    }

    private int setup(Vertex vx) {
        if(isColored(vx))
            return 0;

        int n=0;
        setColor(vx, vx.color);
        points.add(vx);
        for (Vertex adj : vx.adjacents) {
            n += setup(adj);
        }

        return n==0 ? 1 : n;
    }

    static final int VALID = 0x4;

    static int getColor(Vertex vx) {
        return vx.color&0x3;
    }

    static void setColor(Vertex vx, int color) {
        vx.color &= ~0x7;
        vx.color |= (color&0x3) | VALID;
    }

    static boolean isColored(Vertex vx) {
        return (vx.color & VALID) != 0;
    }

    static void deColor(Vertex vx) {
        vx.color &= ~VALID;
    }

    static void resetColor(Vertex vx, int color) {
        vx.color &= ~0x3;
        vx.color |= (color&0x3);
    }

    static void resetColor(Vertex vx) {
        vx.color &= 0x3;
    }

    static void markColor(Vertex vx, int color) {
        deColor(vx);
        vx.color |= 0x10<<(color&0x3);
    }

    /**
     * Analyze possible colors for a vertex vx.
     * @param vx vertex to analyze
     * @return a map of possible colors.
     */
    static int validColors(Vertex vx) {
        int cm = 0;

        // analyze all adjacent with already set color.
        for (Vertex va : vx.adjacents) {
            if(isColored(va))
                cm |= 1<<getColor(va);
        }
        // invert
        return cm ^ 0xf;
    }

    public int dye() {

        setup();

        int n = dye(3);

        int stat = 0;
        for (Vertex vx : sphere.points) {
            int k = Integer.bitCount(vx.color & 0xf0);
            stat += 1<<(8*k);
        }

        System.out.format("dyer: %d %08x\n", n, stat);

        sphere.faces.fireChange();

        return n;
    }

    int dye(int index) {

        if(index>=points.size())
            return 1;

        Vertex vx = points.get(index);

        int n = 0;
        // try all possible colors.
        int cm = validColors(vx);
        for(int j=0; j<4; ++j) {
            if((cm&(1<<j))!=0)
                n += dye(index, j);
        }

        return n;
    }

    /**
     * try to dye this vertex by a given color and try to dye all uncolored adjacent.
     * @param index to dye
     * @param color color to set
     * @return number of possible colorings.
     */
    public int dye(int index, int color) {
        Vertex vx = points.get(index);

        int saved = getColor(vx);

        setColor(vx, color);

        int n = dye(index+1);

        if(n>0) // mark valid color and release
            markColor(vx, color);
        else // reset to previous value and release
            resetColor(vx, saved);

        return n;
    }
}
