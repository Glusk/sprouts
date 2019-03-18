package com.github.glusk2.sprouts.comb;

import java.util.List;
import java.util.Set;

/**
 * A connected planar graph with a set of edges and vertices.
 */
public interface PlanarGraph {
    /**
     * Returns graph edges.
     *
     * @return a set of {@code this} graph's edges
     */
    Set<Edge> edges();

    /**
     * Returns graph vertices.
     *
     * @return a set of {@code this} graph's vertices
     */
    Set<Vertex> vertices();

    /**
     * Returns graph faces.
     *
     * @return a list of {@code this} graph's faces
     */
    List<Face> faces();
}
