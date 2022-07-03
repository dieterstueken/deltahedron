package ditz.atrops.cube;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntUnaryOperator;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 03.07.22
 * Time: 11:37
 */
public class Perm implements Comparable<Perm> {

    final int perm;

    public static Perm perm(IntUnaryOperator p) {

        int m = 0;
        for(int i=0; i<8; ++i) {
            int k = p.applyAsInt(i);
            m |= k<<(4*i);
        }

        return perm(m);
    }

    public static Perm perm(int perm) {
        return new Perm(perm);
    }

    private Perm(int perm) {
        this.perm = perm;
    }

    @Override
    public int compareTo(Perm o) {
        return Integer.compare(perm, o.perm);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Perm p && perm == p.perm;
    }

    @Override
    public int hashCode() {
        return perm;
    }

    @Override
    public String toString() {
        return String.format("%08x", perm&0xffffffffL);
    }

    public int get(int pos) {
        return (perm >>> (4*pos)) & 0x7;
    }

    public Perm apply(Perm p) {
        return perm(i->get(p.get(i)));
    }

    public Perm mirror(int axis) {
        int m = 1 << (axis%3);
        m *= 0x11111111;
        return perm(perm^m);
    }

    public Perm swap(int i, int j) {
        return perm(
                k -> {
                    if(k==i) return j;
                    if(k==j) return i;
                   return k;
                }
        );
    }

    public static final Perm P0 = perm(i -> i);

    public static final Perm RX = perm(0x32761054);
    public static final Perm RY = perm(0x37152604);
    public static final Perm RZ = perm(0x57461302);

    static final List<Perm> PERMS = List.of(RX, RY, RZ);

    public static void main(String ... args) {

        Set<Perm> perms = new TreeSet<>();
        
        for (Perm p0 : PERMS) {
            for (Perm p1 : PERMS) {
                Perm m2 = p1.apply(p0);
                perms.add(m2);
                for (Perm p2 : PERMS) {
                    Perm m3 = p2.apply(m2);
                    perms.add(m3);
                    for (Perm p4 : PERMS) {
                        Perm m4 = p4.apply(m3);
                        perms.add(m4);
                        for (Perm p5 : PERMS) {
                            Perm m5 = p5.apply(m4);
                            perms.add(m5);
                        }
                    }
                }
            }
        }

        System.out.format("%d permutations\n", perms.size());

        for (Perm perm : perms) {
            System.out.println(perm);
        }

    }

}
