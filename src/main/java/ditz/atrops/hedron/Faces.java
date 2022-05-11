package ditz.atrops.hedron;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  01.05.2022 13:05
 * modified by: $
 * modified on: $
 */
public interface Faces {

    Face newFace(Vertex v0, Vertex v1, Vertex v2);

    void updateFace(Face face);

    Face addFace(Face face);

    Face removeFace(Face face);

    boolean addVertex(Vertex v);
}
