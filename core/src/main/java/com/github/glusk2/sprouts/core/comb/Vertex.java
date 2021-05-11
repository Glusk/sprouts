package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.math.Vector2;

/**
 * A Graph vertex.
 * <p>
 * Objects that implement this interface must also override hashCode() and
 * equals().
 */
public interface Vertex extends ColorTag {
    /**
     * Returns the position of {@code this} vertex in a 2-dimensional space.
     * <p>
     * Implementations should return a copy of the position vector to ensure
     * immutability of {@code this} object -
     * {@link com.badlogic.gdx.math.Vector2#cpy()}.
     *
     * @return a copy of {@code this} vertex position vector
     */
    Vector2 position();
}
