package com.github.glusk2.sprouts.comb;

import java.util.List;
import java.util.Set;

import com.github.glusk2.sprouts.Drawable;

/**
 * A Graph is a combinatorial representation of a game of Sprouts.
 */
public interface Graph extends Drawable {
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
}
