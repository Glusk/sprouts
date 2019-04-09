package com.github.glusk2.sprouts.comb;

import java.util.List;

/**
 * Represents a graph Vertex and a list of VertexPlugs connected to it. It can
 * be thought of as a clock with many clock pointers. All pointers
 * (VertexPlugs) are connected to the center (center Vertex).
 */
public interface LocalRotations {
    /**
     * Converts the internal representation of {@code this} object into a list
     * of DirectedEdges that share a common source Vertex (the center Vertex
     * of {@code this} LocalRotations), ordered by their destination vertices
     * in clockwise order around the source Vertex, and returns it.
     *
     * @return an ordered list of {@code this} object's clock pointers
     */
    List<DirectedEdge> edges();

    /**
     * Advances the clock pointer {@code current} to the next pointer in
     * {@code this} LocalRotations and returns the result as a new DirectedEdge
     * that goes from the center Vertex of {@code this} LocalRotations to the
     * {@code endPoint()} of the next clock pointer.
     * <p>
     * {@code current} need not be a part of {@code this} LocalRotations.
     *
     * @param current the clock pointer that starts at the center Vertex of
     *                {@code this} LocalRotations object
     * @return a new DirectedEdge that goes from the center Vertex of
     *         {@code this} LocalRotations to the {@code endPoint()} of the
     *         first clock pointer after {@code current}
     */
    DirectedEdge next(VertexPlug current);

    /**
     * Returns new LocalRotations with {@code plugToAdd}.
     *
     * @param plugToAdd the plug to add
     * @return new LocalRotations with {@code plugToAdd}
     */
    LocalRotations with(VertexPlug plugToAdd);

    /**
     * Returns new LocalRotations without {@code plugToRemove}.
     *
     * @param plugToRemove the plug to remove
     * @return new LocalRotations without {@code plugToRemove}
     */
    LocalRotations without(VertexPlug plugToRemove);
}
