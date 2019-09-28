package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * A DirectedEdge of a graph is a directed connection between 2 of its
 * vertices.
 * <p>
 * Objects that implement this interface must also override hashCode() and
 * equals().
 */
public interface DirectedEdge extends Colorable {
    /**
     * Returns the source Vertex of {@code this} DirectedEdge. The source
     * Vertex represents the <em>beginning</em> of a graph connection.
     *
     * @return the source Vertex of {@code this} DirectedEdge
     */
    Vertex from();
    /**
     * Returns the destination Vertex of {@code this} DirectedEdge. The
     * destination Vertex represents the <em>end</em> of a graph connection.
     *
     * @return the destination Vertex of {@code this} DirectedEdge
     */
    Vertex to();
    /**
     * {@inheritDoc}
     * <p>
     * The color of a DirectedEdge is the color of the line between
     * {@code from()} and {@code to()}.
     */
    Color color();

    /**
     * Returns the geometric representation of {@code this} DirectedEdge.
     * <p>
     * A geometric representation of a DirectedEdge is a straight line (or
     * a curve approximated to line segments) bounded by {@code from()} and
     * {@code to()}.
     *
     * @return the geometric representation of {@code this} DirectedEdge
     */
    Polyline polyline();
}
