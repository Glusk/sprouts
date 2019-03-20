package com.github.glusk2.sprouts.comb;

import java.util.List;
import java.util.Set;

import com.github.glusk2.sprouts.geom.Polyline;

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

    /**
     * Returns the face in which {@code nextMove} is being drawn.
     *
     * @param nextMove the next stroke a player draws from an existing
     *                 "sprout"
     * @return the face in which {@code nextMove} is being drawn
     * @throws IllegalArgumentException if the move is invalid
     */
    Face nextMoveFace(Polyline nextMove) throws IllegalArgumentException;
}
