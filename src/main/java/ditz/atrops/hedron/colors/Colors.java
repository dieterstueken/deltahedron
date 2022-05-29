package ditz.atrops.hedron.colors;

import ditz.atrops.hedron.Face;
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

    static final List<Color> COLORS = List.of(
            Color.GOLD,
            Color.PALEVIOLETRED,
            Color.FORESTGREEN,
            Color.AQUA);

    static final ColorGrid GRID = new ColorGrid();

    Color color(int ix, int iy) {
        int icol = Dot.icol((ix+0.5)/size, (iy+0.5)/size);

        if((icol&4)!=0)
            return Color.BLACK;

        if((icol&8)!=0)
            return COLORS.get(icol%4);

        return Color.WHITE;
    }

    final int size;

    final WritableImage image;

    public Colors(int size) {

        this.size = size-1;
        
        this.image = new WritableImage(size, size);
        PixelWriter pw  = image.getPixelWriter();

        //icol(0.3, 0.7);

        for(int j=0; j<size; ++j) {
            for(int i=0; i<size; ++i) {
                Color color = color(i, j);
                pw.setColor(i, j, color);
            }
        }
    }

    /**
     * Create 19 coordinate pairs.
     * @return texture coordinates.
     */
    public float[] coords() {

        int n = GRID.dots.size();

        float[] coords = new float[2*n];

        List<Dot> dots = GRID.dots;
        for (int i = 0; i < dots.size(); i++) {
            Dot dot = dots.get(i);
            coords[2*i] = dot.x;
            coords[2*i+1] = dot.y;
        }

        return coords;
    }

    /**
     * Lookup Tex coordinate index for edge of index.
     * @param face to colorize.
     * @param index of edge.
     * @return texture coordinate index.
     */
    public int getTex(Face face, int index) {
        
        Triplet t = GRID.getTriplet(
                face.getColor(0),
                face.getColor(1),
                face.getColor(2));

        return t.getDot(index).getIndex();
    }

    public PhongMaterial getMaterial() {
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseMap(image);
        return mat;
    }

    public static void main(String ... args) throws IOException {

        Colors colors = new Colors(600);

        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(colors.image, null), "png", new File("palette.png"));
    }
}
