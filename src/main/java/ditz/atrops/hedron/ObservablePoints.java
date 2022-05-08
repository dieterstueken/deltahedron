package ditz.atrops.hedron;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ObservablePoints extends AbstractObservableFloats {

    private final ObservableList<Vertex> points;

    public ObservablePoints(ObservableList<Vertex> points) {
        this.points = points;
        this.points.addListener(this::onChanged);
    }

    void onChanged(ListChangeListener.Change<? extends Vertex> c) {
        while (c.next()) {
            System.out.print("points ");
            System.out.println(c);
        }
    }

    @Override
    public int size() {
        return 3*points.size();
    }

    @Override
    public float get(int index) {
        Vertex v = points.get(index/3);
        double coord = v.getCoord(index%3);
        return (float) coord;
    }
}
