package com.github.glusk2.sprouts.comb;

import java.util.Set;

/**
 * An Edge is a pair of opposite DirectedEdges.
 * <p>
 * Opposite DirectedEdges have their source and destination Vertices reversed:
 * {@code opposite( (a, b) ) = (b, a)}.
 * <p>
 * Objects that implement this interface must also override hashCode() and
 * equals().
 */
public interface Edge {
    /**
     * Returns a pair of opposite DirectedEdges that form {@code this} Edge.
     *
     * @return a new set of opposite DirectedEdges; the size of the
     *         returned set may be equal to 1 if the end Vertices of
     *         {@code this} Edge equal
     */
    Set<DirectedEdge> oppositePair();
}
