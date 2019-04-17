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
    List<Set<DirectedEdge>> faces();

    /**
     * Returns a list of LocalRotations of {@code this} Graph.
     *
     * @return a new list of LocalRotations of {@code this} Graph
     */
    RotationsList rotationsList();
}
