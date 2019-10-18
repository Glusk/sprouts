package com.github.glusk2.sprouts.core.comb;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.github.glusk2.sprouts.core.util.RenderBatch;

/**
 * A Graph is a combinatorial representation of a game of Sprouts.
 */
public interface Graph extends RenderBatch {
    /**
     * Returns the faces of this Graph.
     * <p>
     * Each <a href="https://proofwiki.org/wiki/Definition:Planar_Graph/Face">face</a>
     * is a Set of its bounding DirectedEdges.
     *
     * @return the list of bounding Sets of {@code this} Graph's faces
     */
    List<Set<CompoundEdge>> faces();

    /**
     * A set of LocalRotations CompoundEdges {@code v, (a, b)} of {@code this}
     * Graph.
     * <p>
     * Refer to {@link LocalRotations} interface definition for more info on
     * notation used.
     *
     * @return the set of CompoundEdges {@code v, (a, b)}
     */
    Set<CompoundEdge> edges();

    /**
     * Returns {@code this} Graph's Vertices.
     *
     * @return the Set of Vertices in {@code this} Graph
     */
    Set<Vertex> vertices();

    /**
     * Creates and returns a new Graph with the {@code additionalVertex}.
     * <p>
     * If {@code additionalVertex} is already in {@code this} Graph, the method
     * returns {@code this}.
     * @param additionalVertex the Vertex to add to {@code this} Graph
     * @return a new Graph with the {@code additionalVertex}
     */
    Graph with(Vertex additionalVertex);
    /**
     * Returns a new Graph with an additional DirectedEdge.
     * <p>
     * Refer to {@link LocalRotations} interface definition for notes on
     * notation.
     *
     * @param origin the center Vertex {@code v}
     * @param direction the DirectedEdge {@code (a, b)}
     * @return a new Graph with an additional DirectedEdge
     */
    Graph with(Vertex origin, DirectedEdge direction);

     /**
     * Returns a new Graph without a surplus DirectedEdge.
     * <p>
     * Refer to {@link LocalRotations} interface definition for notes on
     * notation.
     *
     * @param origin the center Vertex {@code v}
     * @param direction the DirectedEdge {@code (a, b)}
     * @return a new Graph without a surplus DirectedEdge
     */
    Graph without(Vertex origin, DirectedEdge direction);

    /**
     * Returns the face in which {@code edge} lies.
     * <p>
     * Refer to {@link LocalRotations} interface definition for notes on
     * notation.
     *
     * @param edge a CompoundEdge {@code v, (a, b)}
     * @return the face in which {@code edge} lies
     */
    Set<CompoundEdge> edgeFace(CompoundEdge edge);

    /**
     * Returns a new graph without unneeded faces, vertices or edges.
     *
     * @return a simplified Graph
     */
    Graph simplified();

    /**
     * Checks whether {@code vertex} is a living sprout connected to
     * {@code this} Graph.
     * <p>
     * A sprout is said to be alive when it is connected to at most 3
     * Submove edges.
     *
     * @param vertex the Vertex to check
     * @return {@code true} if {@code vertex} is a living sprout connected to
     *         {@code this} Graph
     */
    boolean isAliveSprout(Vertex vertex);

    /**
     * Returns the number of {@code edgeColor} edges that {@code vertex} is
     * connected to in {@code this} Graph.
     *
     * @param vertex the Vertex to look up the degree for
     * @param edgeColor the degree is computed by only considering
     *                  {@code edgeColor} edges
     * @return a negative integer, if {@code vertex} is not connected to
     *         {@code this} Graph, the number of {@code edgeColor} edges
     *         connected to {@code vertex} otherwise
     */
    int vertexDegree(Vertex vertex, Color edgeColor);
}
