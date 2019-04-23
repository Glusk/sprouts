package com.github.glusk2.sprouts.comb;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.geom.Polyline;

/** A DirectedEdge extended by an additional Vertex. */
public final class ExtendedEdge implements DirectedEdge {
    /**
     * A boolean flag.
     * <p>
     * If {@code true} the {@code vertexToAdd} will be added to the end of
     * {@code existingEdge}, else to the start.
     */
    private final boolean appendToEnd;
    /** The Vertex to append to DirectedEdge. */
    private final Vertex vertexToAdd;
    /** The original DirectedEdge. */
    private final DirectedEdge existingEdge;

    /**
     * Constructs a new DirectedEdge from {@code existingEdge} by appending
     * {@code vertexToAdd} at the end of it.
     *
     * @param vertexToAdd the Vertex to append at the end of
     *                    {@code existingEdge}
     * @param existingEdge the original DirectedEdge
     */
    public ExtendedEdge(
        final Vertex vertexToAdd,
        final DirectedEdge existingEdge
    ) {
        this(true, vertexToAdd, existingEdge);
    }

    /**
     * Constructs a new DirectedEdge from {@code existingEdge} by adding
     * {@code vertexToAdd} at the start or the end of it, depending on
     * {@code appendToEnd}.
     *
     * @param appendToEnd if {@code true} the {@code vertexToAdd} will be added
     *                    to the end of {@code existingEdge}, else to the start
     * @param vertexToAdd the Vertex to append at the end or the start of
     *                    {@code existingEdge}
     * @param existingEdge the original DirectedEdge
     */
    public ExtendedEdge(
        final boolean appendToEnd,
        final Vertex vertexToAdd,
        final DirectedEdge existingEdge
    ) {
        this.appendToEnd = appendToEnd;
        this.vertexToAdd = vertexToAdd;
        this.existingEdge = existingEdge;
    }

    @Override
    public Vertex from() {
        if (appendToEnd) {
            return existingEdge.from();
        }
        return vertexToAdd;
    }

    @Override
    public Vertex to() {
        if (appendToEnd) {
            return vertexToAdd;
        }
        return existingEdge.to();
    }

    @Override
    public Color color() {
        return existingEdge.color();
    }

    @Override
    public Polyline polyline() {
        List<Vector2> addToEnd = existingEdge.polyline().points();
        if (appendToEnd) {
            addToEnd.add(vertexToAdd.position());
            return new Polyline.WrappedList(addToEnd);
        }
        List<Vector2> addToStart = new ArrayList<Vector2>();
        addToStart.add(vertexToAdd.position());
        addToStart.addAll(addToEnd);
        return new Polyline.WrappedList(addToStart);
    }

    @Override
    public int hashCode() {
        if (appendToEnd) {
           return (
               from().hashCode() + "-" + vertexToAdd.hashCode()
            ).hashCode();
        }
        return (vertexToAdd.hashCode() + "-" + to().hashCode()).hashCode();
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
        if (appendToEnd) {
            return from().equals(that.from()) && vertexToAdd.equals(that.to());
        }
        return vertexToAdd.equals(that.from()) && to().equals(that.to());
    }
}
