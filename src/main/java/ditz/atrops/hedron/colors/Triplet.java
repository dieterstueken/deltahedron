package ditz.atrops.hedron.colors;

import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 29.05.22
 * Time: 23:58
 */
class Triplet {

    final Dot d0, d1, d2;

    Triplet(Dot d0, Dot d1, Dot d2) {
        this.d0 = d0;
        this.d1 = d1;
        this.d2 = d2;
    }

    public Dot getDot(int i) {
        return switch (i % 3) {
            case 1, -2 -> d1;
            case 2, -1 -> d2;
            default -> d0;
        };
    }


    public int getColor(int i) {
        return getDot(i).icol;
    }

    Stream<Triplet> permute() {
        return Stream.of(this,
                new Triplet(d1, d2, d0),
                new Triplet(d2, d0, d1),
                new Triplet(d1, d0, d2),
                new Triplet(d2, d1, d0));
    }

    public static int colors(int i0, int i1, int i2) {
        return (i0&3) + 4*((i1&3) + 4*(i2&3));
    }

    public int colors() {
        return colors(d0.icol, d1.icol, d2.icol);
    }
    
    @Override
    public String toString() {
        return d0.toString() + '/' + d1.toString() + '/' + d2.toString();
    }
}
