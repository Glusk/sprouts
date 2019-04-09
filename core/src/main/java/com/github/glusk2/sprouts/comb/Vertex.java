package com.github.glusk2.sprouts.comb;

import com.badlogic.gdx.math.Vector2;

/** Graph vertex. */
public interface Vertex extends Colorable {
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

    /**
     * Returns a numeric label of {@code this} vertex for human-readable
     * comparisons.
     *
     * @return an integer label of {@code this} vertex
     */
    int label();
}
