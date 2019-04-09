package com.github.glusk2.sprouts.comb;

import com.badlogic.gdx.graphics.Color;

/**
 * Vertex Plug is a half-line which can be attached to any Vertex to form a
 * {@link DirectedEdge}
 * <p>
 * Objects that implement this interface must also override hashCode() and
 * equals().
 *
 * @see com.github.glusk2.sprouts.comb.DirectedEdge
 */
public interface VertexPlug extends Colorable {
    /**
     * Returns the end-point of {@code this} VertexPlug half-line.
     *
     * @return the Vertex delimiting {@code this} VertexPlug half-line
     */
    Vertex endPoint();
    /**
     * {@inheritDoc}
     * <p>
     * The color of a VertexPlug is the color of its half-line.
     */
    Color color();
}
