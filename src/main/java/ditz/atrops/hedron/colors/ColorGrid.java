package ditz.atrops.hedron.colors;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 29.05.22
 * Time: 23:57
 */
class ColorGrid {

    static Dot d(int i, int j) {
        return new Dot(3 * i + 1, 3 * j + 2);
    }

    static Dot d(int ij) {
        return d((ij >> 4) & 0xf, ij & 0x0f);
    }

    Triplet t(int i0, int i1, int i2) {
        return new Triplet(dots.get(i0), dots.get(i1), dots.get(i2));
    }

    // hexagon of dots
    final List<Dot> dots = List.of(
            d(0x00), d(0x10), d(0x20),
            d(0x01), d(0x11), d(0x21), d(0x31),
            d(0x02), d(0x12), d(0x22), d(0x32), d(0x42),
            d(0x13), d(0x23), d(0x33), d(0x43),
            d(0x24), d(0x34), d(0x44));

    final List<Triplet> trips;

    public ColorGrid() {
        for (int i = 0; i < dots.size(); i++) {
            Dot dot = dots.get(i);
            dot.setIndex(i);
        }

        Triplet[] trips = new Triplet[64];
        baseTrips().stream().flatMap(Triplet::permute).forEach(t->trips[t.colors()]=t);
        this.trips = List.of(trips);
    }
    
    /**
     * Lookup a Triplet for given colors.
     */
    public Triplet getTriplet(int i0, int i1, int i2) {
        int m = Triplet.colors(i0, i1, i2);
        return trips.get(m);
    }

    private List<Triplet> baseTrips() {
        return List.of(
                t(0, 4, 1),    // RGY
                t(1, 5, 2),    // YYY

                t(3, 4, 0),    // GGY
                t(4, 5, 1),    // GYY
                t(5, 6, 2),    // YRY

                t(3, 8, 4),    // GGG
                t(4, 9, 5),    // GBY
                t(5, 10, 6),   // YBR

                t(7, 8, 3),    // YGG
                t(8, 9, 4),    // GBG
                t(9, 10, 5),   // BBY
                t(10, 11, 6),  // BRR

                t(7, 12, 8),   // YRG
                t(8, 13, 9),   // GRB
                t(9, 14, 10),  // BBB
                t(10, 15, 11), // BGR

                t(12, 13, 8),  // RRG
                t(13, 14, 9),  // RBB
                t(14, 15, 10), // BGB

                t(12, 16, 13), // RRR
                t(13, 17, 14), // RYB
                t(14, 18, 15), // BYG

                t(16, 17, 13), // RYR
                t(17, 18, 14));  // YYB

    }

}
