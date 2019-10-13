package com.github.glusk2.sprouts.core.comb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/** A CompoundEdge decorator that is the reversal of original. */
public final class ReversedCompoundEdge implements CompoundEdge {

    /** The CompoundEdge to reverse. */
    private final CompoundEdge original;

    /**
     * A utility constructor that wraps {@code origin} and {@code direction}
     * in a new CompoundEdge.
     *
     * @param origin the origin Vertex {@code v}
     * @param direction the direction DirectedEdge {@code (a, b)}
     */
    public ReversedCompoundEdge(
        final Vertex origin,
        final DirectedEdge direction
    ) {
        this(new CompoundEdge.Wrapped(origin, direction));
    }

    /**
     * Constructs a new CompoundEdge, which is a reversal of {@code original}.
     *
     * @param original the CompoundEdge to reverse
     */
    public ReversedCompoundEdge(final CompoundEdge original) {
        this.original = original;
    }

    @Override
    public Vertex origin() {
        return original.direction().to();
    }

    @Override
    public DirectedEdge direction() {
        List<Vector2> edgePoints = new ArrayList<Vector2>();
        edgePoints.add(original.origin().position());
        edgePoints.addAll(original.direction().polyline().points());
        Collections.reverse(edgePoints);
        return
            new PolylineEdge(
                original.origin().color(),
                original.origin().color(),
                original.direction().color(),
                edgePoints.subList(1, edgePoints.size())
            );
    }

    @Override
    public int hashCode() {
        return new CompoundEdge.Wrapped(origin(), direction()).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return new CompoundEdge.Wrapped(origin(), direction()).equals(obj);
    }
}
