package com.github.glusk2.sprouts.comb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.geom.Polyline;

/**
 * Represents a straight line directed Graph edge between {@code from()} and
 * {@code to()}.
 */
public final class StraightLineEdge implements DirectedEdge {
    /** Default Edge color. */
    private static final Color DEFAULT_COLOR = Color.RED;
    /** The color of {@code this} DirectedEdge.*/
    private final Color color;
    /** The source Vertex of {@code this} DirectedEdge. */
    private Vertex from;
    /** The destination Vertex of {@code this} DirectedEdge. */
    private Vertex to;

    /**
     * Constructs a new StraightLineEdge with equal endpoints and
     * the default edge color.
     *
     * @param vertex the endpoints of the edge
     */
    public StraightLineEdge(final Vertex vertex) {
        this(vertex, vertex);
    }

    /**
     * Constructs a new StraightLineEdge with equal endpoints and
     * the specified edge color.
     *
     * @param color the color of the DirectedEdge
     * @param vertex the endpoints of the edge
     */
    public StraightLineEdge(final Color color, final Vertex vertex) {
        this(color, vertex, vertex);
    }

    /**
     * Constructs a new StraightLineEdge from its endpoints and the default
     * edge color.
     *
     * @param from the source Vertex of the DirectedEdge
     * @param to the destination Vertex of the DirectedEdge
     */
    public StraightLineEdge(final Vertex from, final Vertex to) {
        this(DEFAULT_COLOR, from, to);
    }

    /**
     * Constructs a new StraightLineEdge from its endpoints and the specified
     * edge color.
     *
     * @param color the color of the DirectedEdge
     * @param from the source Vertex of the DirectedEdge
     * @param to the destination Vertex of the DirectedEdge
     */
    public StraightLineEdge(
        final Color color,
        final Vertex from,
        final Vertex to) {
        this.color = color;
        this.from = from;
        this.to = to;
    }

    @Override
    public Vertex from() {
        return from;
    }

    @Override
    public Vertex to() {
        return to;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public Polyline polyline() {
        Set<Vector2> endpoints = new HashSet<Vector2>();
        endpoints.add(from().position());
        endpoints.add(to().position());
        return new Polyline.WrappedList(new ArrayList<Vector2>(endpoints));
    }

    @Override
    public int hashCode() {
        return (
            color().hashCode()
            + "-"
            + from().hashCode()
            + "-"
            + to().hashCode()
        ).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof DirectedEdge)) {
            return false;
        }
        DirectedEdge that = (DirectedEdge) obj;
        return
            color().equals(that.color())
            && from().equals(that.from())
            && to().equals(that.to());
    }
}
