package ditz.atrops.hedron;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.List;

public class Palette {

    public static final Palette DEFAULT = Palette.of(
            Color.BLACK, Color.GRAY, Color.WHITE,
            Color.RED, Color.GREEN, Color.BLUE,
            Color.YELLOW, Color.FUCHSIA, Color.AQUA);

    final List<Color> palette;

    public static Palette of(Color ... palette) {
        return new Palette(List.of(palette));
    }

    public Palette(List<Color> palette) {
        this.palette = palette;
    }

    public Image getImage() {
        int size = palette.size();
        WritableImage img = new WritableImage(size, 1);
        PixelWriter pw  = img.getPixelWriter();

        for(int i=0; i<size; ++i) {
            pw.setColor(i, 0, palette.get(i));
        }

        return img;
    }

    public AbstractObservableFloats getTextPoints() {

        return new AbstractObservableFloats() {

            @Override
            public int size() {
                return 2*palette.size();
            }

            @Override
            public float get(int index) {
                if((index%2)==0)
                    return (index + 0.5F)/size();
                else
                    return 0.5F;
            }
        };
    }

    public PhongMaterial createMaterial() {
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseMap(getImage());
        return mat;
    }
}
