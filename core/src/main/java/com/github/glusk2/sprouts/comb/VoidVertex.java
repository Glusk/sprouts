package com.github.glusk2.sprouts.comb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * This class represents a non-existent Vertex. Its color is
 * {@code Color.CLEAR} and invocations of {@code position()} throw
 * an {@code IllegalStateException}.
 * <p>
 * VoidVertex is an alternative to {@code null} references and exceptions
 * with the hope to make for a more readable code. Using exceptions for
 * <em>flow-control</em> is a known anti-pattern!
 */
public final class VoidVertex implements Vertex {
    /** String label of {@code this} vertex. */
    private final String label;

    /**
     * Constructs a new VoidVertex with the specified {@code label}.
     *
     * @param label string label of the vertex.
     */
    public VoidVertex(final String label) {
        this.label = label;
    }

    /**
     * Returns {@code Color.CLEAR} as VoidVertex objects have no color.
     *
     * @return returns {@code Color.CLEAR}
     */
    @Override
    public Color color() {
        return Color.CLEAR;
    }

    /**
     * Since VoidVertex by definition has no position, this method always
     * throws an {@code IllegalStateException}.
     *
     * @return throws an {@code IllegalStateException}
     */
    @Override
    public Vector2 position() {
        throw new IllegalStateException("A VoidVertex has no position");
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public int hashCode() {
        return (color().hashCode() + "-" + 0).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof VoidVertex)) {
            return false;
        }
        return true;
    }
}
