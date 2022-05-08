package ditz.atrops.hedron;

import javafx.collections.ObservableList;

public class ObservableFaces extends AbstractObservableIntegers {

    private final ObservableList<Face> faces;

    public ObservableFaces(ObservableList<Face> faces) {
        this.faces = faces;
        this.faces.addListener(this::onChanged);
    }

    @Override
    public int size() {
        return 6* faces.size();
    }

    @Override
    public int get(int index) {
        Face f = faces.get(index/6);

        return switch (index % 6) {
            case 0 -> f.v0.index;
            case 2 -> f.v1.index;
            case 4 -> f.v2.index;
            default -> 3 + (f.color)%6;
        };
    }
}
