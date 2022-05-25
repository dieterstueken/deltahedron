package ditz.atrops.hedron;

import javafx.geometry.Point3D;

import java.util.function.Predicate;

public class Points extends ObservablePoints {

    final Faces faces;

    public Points(Faces faces) {
        this.faces = faces;
    }

    /**
     * Add a new vertex and create new faces to connect.
     * @param vertex to add.
     * @return
     */
    @Override
    public boolean add(Vertex vertex) {
        super.add(vertex);
        connect(vertex);

        assert verify(this::connectivity);

        return true;
    }

    /**
     * Remove a vertex and close the gap with new faces.
     * @param index the index of the element to be removed
     * @return the vertex removed.
     */
    @Override
    public Vertex remove(int index) {

        // drop edges first
        final Vertex vx = get(index);

        faces.cutOff(vx);

        Vertex removed = super.remove(index);

        assert verify(this::connectivity);

        return removed;
    }

    @Override
    public boolean verify(Predicate<? super Vertex> verify) {

        faces.verify(this::validateFace);

        return super.verify(verify);
    }

    protected boolean validateFace(Face face) {
        boolean verified =  face.verify();

        if(size()>3) {

            for (Vertex vertex : face.points) {

                if(vertex.isValid() && get(vertex.getIndex())==vertex) {

                    if (vertex.faces.size() < 3) {
                        verified = false;
                        break;
                    }

                    assert verified : "too few faces on vertex";

                } else
                    verified = false;

                assert verified : "invalid vertex point";
            }
        }

        return verified;
    }

    protected boolean invalidate(Vertex vertex) {
        assert !vertex.isValid() || vertex.faces.isEmpty();
        return super.invalidate(vertex);
    }

    protected boolean connectivity(Vertex vertex) {
        boolean verified =  vertex.verify();

        if(size()>3 && vertex.faces.size()<3) {
            assert false : "too few faces on vertex";
            verified = false;
        }

        return verified;
    }

    /**
     * Setup a new index of a vertex.
     * If the vertex existed before, update the faces, too.
     *
     * @param vertex to index.
     * @param index to set.
     * @return
     */
    @Override
    protected boolean setup(Vertex vertex, int index) {
        boolean setup = super.setup(vertex, index);

        // update all faces
        vertex.faces.forEach(faces::update);

        return setup;
    }

    public Vertex addPoint(Point3D point) {

        // reject too close points
        if (stream().anyMatch(v -> v.squareDist(point) * (1L << 32) < 1))
            return null;

        Vertex vx = new Vertex(size(), point);
        add(vx);

        assert verify(this::connectivity);

        return vx;
    }

    /**
     * Connect a new vertex to all visible faces.
     * Hidden faces are removed.
     * If this is the 3rd vertex, two flat dummy faces are created.
     *
     * @param vx element whose presence in this collection is to be ensured
     */
    private void connect(Vertex vx) {

        int size = size();

        if(size==3) {
            Vertex v0 = get(0);
            Vertex v1 = get(1);

            faces.addFace(v0, v1, vx);
            faces.addFace(v1, v0, vx);
        } else if(size>3) {
            boolean connected = faces.addVertex(vx);
            if(!connected)
                throw new IllegalStateException("vertex not connected");
        }
    }
}
