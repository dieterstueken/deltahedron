package ditz.atrops.hedron;

import javafx.geometry.Point3D;

public class Points extends ObservablePoints {

    final Faces faces;

    public Points(Faces faces) {

        this.faces = faces;
    }

    @Override
    public Vertex set(int index, Vertex vertex) {
        /**
         * element possibly gets a new index:
         * update all related faces.
         */
        try {
            return super.set(index, vertex);
        } finally {
            if(index!=vertex.getIndex())
                vertex.faces.forEach(faces::updateFace);
        }
    }

    public Vertex addPoint(Point3D point) {

        // reject too close points
        if (stream().anyMatch(v -> v.squareDist(point) * (1L << 32) < 1))
            return null;

        int id = size();
        Vertex vx = new Vertex(id, point);
        add(vx);
        return vx;
    }

    /**
     * Add a vertex and update faces.
     * @param vx element whose presence in this collection is to be ensured
     * @return
     */
    public boolean add(Vertex vx) {
        super.add(vx);

        int size = size();

        if(size==3) {
            Vertex v0 = get(0);
            Vertex v1 = get(1);

            faces.newFace(v0, v1, vx);
            faces.newFace(v1, v0, vx);
        } else if(size>3) {
            boolean connected = faces.addVertex(vx);
            if(!connected)
                throw new IllegalStateException("vertex not connected");
        }

        return true;
    }

    public boolean remove(Vertex v) {
        cutOff(v);

        if(!super.remove(v))
            throw new IllegalStateException("face not registered");

        return true;
    }

    @Override
    public boolean remove(Object obj) {
        return obj instanceof Vertex && remove((Vertex) obj);
    }

    void cutOff(Vertex v) {
        throw new RuntimeException("not implemented");

        /*
         * get list of outer edges (= faces:opposite points)
         * while(n>3)
         *      find adjacent faces, which can be joined without conflicts
         *          drop both faces and replace by a new outer face
         *          and a temporary inner face.
         *
         * n==3:
         *      replace all by a new single face.
         *
         * n<3:
         *     drop faces.
         */
    }
}
