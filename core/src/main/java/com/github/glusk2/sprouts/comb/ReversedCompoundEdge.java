package com.github.glusk2.sprouts.comb;

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
        DirectedEdge result =
            new StraightLineEdge(
                original.direction().color(),
                original.origin()
            );
        Vector2[] points =
            original.direction()
                    .polyline()
                    .points()
                    .toArray(new Vector2[0]);
        for (int i = 0; i < points.length - 1; i++) {
            result =
                new ExtendedEdge(
                    false,
                    new PresetVertex(
                        original.direction().color(),
                        points[i],
                        points[i].toString()
                    ),
                    result
                );
        }
        return result;
    }
    @Override
    public int hashCode() {
        return (origin().hashCode() + "-" + direction().hashCode()).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof CompoundEdge)) {
            return false;
        }
        CompoundEdge that = (CompoundEdge) obj;
        return
            origin().equals(that.origin())
            && direction().equals(that.direction());
    }
}
