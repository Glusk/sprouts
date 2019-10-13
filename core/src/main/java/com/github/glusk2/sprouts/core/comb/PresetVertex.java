package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/** Read-only, struct-like Vertex implementation. */
public final class PresetVertex implements Vertex {
    /** The default Vertex label. */
    private static final String DEFAULT_LABEL = "<unlabeled>";
    /** The default Vertex color. */
    private static final Color DEFAULT_COLOR = Color.BLACK;
    /** The color of {@code this} Vertex. */
    private final Color color;
    /** Position of {@code this} Vertex in a 2-dimensional space. */
    private final Vector2 position;
    /** Numeric label of {@code this} vertex. */
    private final String label;

    /**
     * Constructs a new Vertex with the specified position and the default
     * color and label.
     *
     * @param position position of the Vertex in a 2-dimensional space
     */
    public PresetVertex(final Vector2 position) {
        this(position, DEFAULT_LABEL);
    }

    /**
     * Constructs a new Vertex with the specified color and position, using the
     * default label.
     *
     * @param color color of the Vertex
     * @param position position of the Vertex in a 2-dimensional space
     */
    public PresetVertex(
        final Color color,
        final Vector2 position
    ) {
        this(color, position, DEFAULT_LABEL);
    }
    /**
     * Constructs a new Vertex with specified {@code position} and a numeric
     * {@code label}. The color of {@code this} Vertex is set to default.
     *
     * @param position position of the Vertex in a 2-dimensional space
     * @param label numeric label of the Vertex
     */
    public PresetVertex(final Vector2 position, final Number label) {
        this(DEFAULT_COLOR, position, label.toString());
    }

    /**
     * Constructs a new Vertex with specified {@code position} and a string
     * {@code label}. The color of {@code this} Vertex is set to default.
     *
     * @param position position of the Vertex in a 2-dimensional space
     * @param label string label of the Vertex
     */
    public PresetVertex(final Vector2 position, final String label) {
        this(DEFAULT_COLOR, position, label);
    }

    /**
     * Constructs a new Vertex with specified color, position and label.
     *
     * @param color color of the Vertex
     * @param position position of the Vertex in a 2-dimensional space
     * @param label string label of the Vertex
     */
    public PresetVertex(
        final Color color,
        final Vector2 position,
        final String label
    ) {
        this.color = color;
        this.position = position.cpy();
        this.label = label;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public Vector2 position() {
        return position.cpy();
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public int hashCode() {
        return (
            color().hashCode() + "-" + position().hashCode()
        ).hashCode();
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
