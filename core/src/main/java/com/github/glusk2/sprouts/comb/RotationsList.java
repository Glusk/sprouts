package com.github.glusk2.sprouts.comb;

import java.util.Map;

/**
 * A RotationsList is very similar to <em>adjacency list</em>. The difference
 * is that in a RotationsList the adjacent Vertices are ordered.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Adjacency_list">Adjacency list</a>
 */
public interface RotationsList {
    /**
     * Returns {@code this} RotationsList as a map of Vertices and its
     * LocalRotations.
     *
     * @return map of Vertices and its LocalRotations
     */
    Map<Vertex, LocalRotations> list();
}
