package com.github.glusk2.sprouts.comb;

import java.util.Set;

/**
 * Represents a face of a graph.
 * <p>
 * Implementations should override {@code equals} and {@code hashCode}.
 */
public interface Face {
    /**
     * Returns the boundary of {@code this} face as a set of edges.
     *
     * @return the set of edges surrounding {@code this} face.
     */
    Set<Edge> boundary();
}
