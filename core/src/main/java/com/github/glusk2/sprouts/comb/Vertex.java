package com.github.glusk2.sprouts.comb;

import com.badlogic.gdx.math.Vector2;

/** Read-only, struct-like object representing a graph vertex. */
public final class Vertex {
    /** Position of {@code this} vertex in 2-dimensional space. */
    private final Vector2 position;
    /** Numeric label of the vertex. */
    private final int label;

    /**
     * Constructs a new Vertex object with specified position and label.
     *
     * @param position position of the vertex in 2-dimensional space
     * @param label numeric label of the vertex
     */
    public Vertex(final Vector2 position, final int label) {
        this.position = position;
        this.label = label;
    }

    /**
     * Returns vertex position.
     *
     * @return a copy of vertex position
     */
    public Vector2 position() {
        return position.cpy();
    }

    /**
     * Label getter.
     *
     * @return vertex label
     */
    public int label() {
        return label;
    }

    /**
     * Returns label.
     * <p>
     * Equivalent to:
     * <pre>
     * return label();
     * </pre>
     * <p>
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return label();
    }

    /**
     * Tests 2 Vertices for equality by comparing labels.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Vertex)) {
            return false;
        }
        Vertex that = (Vertex) obj;
        return this.label == that.label;
    }
}
