package ditz.atrops.cube;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 03.07.22
 * Time: 11:27
 */
public class Cube implements Comparable<Cube> {

    public final int edges;

    public Cube(int edges) {
        this.edges = edges;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Cube c && edges == c.edges;
    }

    @Override
    public int hashCode() {
        return edges;
    }

    @Override
    public int compareTo(Cube o) {
        return Integer.compare(edges, o.edges);
    }
}
