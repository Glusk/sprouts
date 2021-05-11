package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/** Read-only, struct-like Vertex implementation. */
public final class PresetVertex implements Vertex {
    /** The default Vertex color. */
    private static final Color DEFAULT_COLOR = Color.BLACK;
    /** The color of {@code this} Vertex. */
    private final Color color;
    /** Position of {@code this} Vertex in a 2-dimensional space. */
    private final Vector2 position;

    /**
     * Constructs a new Vertex with the specified position and the default
     * color.
     *
     * @param position position of the Vertex in a 2-dimensional space
     */
    public PresetVertex(final Vector2 position) {
        this(DEFAULT_COLOR, position);
    }

    /**
     * Constructs a new Vertex with specified color and position.
     *
     * @param color color of the Vertex
     * @param position position of the Vertex in a 2-dimensional space
     */
    public PresetVertex(
        final Color color,
        final Vector2 position
    ) {
        this.color = color;
        this.position = position.cpy();
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public Vector2 position() {
        return position.cpy();
    }

    /**
     * Hashes {@code v} without integer overflow and returns result modulo
     * {@link Integer#MAX_VALUE}.
     * <p>
     * The default {@link Vector2#hashCode()} method overflows and can return
     * the same hash for different vectors. This method tries to alleviate
     * that.
     *
     * @param v a {@code Vector2} object to hash
     * @return the hash code of {@code v}
     */
    public static int vector2HashCode(final Vector2 v) {
        final int prime = 31;
        long result = 1;
        result = prime * result + Float.floatToIntBits(v.x);
        result = prime * result + Float.floatToIntBits(v.y);

        return (int) (result % Integer.MAX_VALUE);
    }

    @Override
    public int hashCode() {
        int h1 = PresetVertex.vector2HashCode(position());
        int h2 = color.hashCode();
        return h1 ^ ((h2 >>> Short.SIZE) | (h2 << Short.SIZE));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Vertex)) {
            return false;
        }
        Vertex that = (Vertex) obj;
        return
            color.equals(that.color())
            && position.equals(that.position());
    }
}
