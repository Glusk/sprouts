package com.github.glusk2.sprouts.core.comb;

import java.util.Comparator;

import com.badlogic.gdx.math.Vector2;

/**
 * Comparator that orders DirectedEdges in clock-wise order given a center
 * Vertex.
 * <p>
 * The order is determined by computing the cross product of vectors:
 * <ul>
 * <li>{@code (v, a)}</li>
 * <li>{@code (v, c)}</li>
 * </ul>
 * <p>
 * Refer to {@link LocalRotations} interface definition for more info on
 * notation used.
 *
 * @see <a href="https://stackoverflow.com/a/6989383">[SO] Sort points in clockwise order?</a>
 */
public final class ClockwiseComparator implements Comparator<DirectedEdge> {
    /** The center Vertex -{@code v} - of {@code this} ClockwiseComparator. */
    private final Vertex center;

    /**
     * Constructs a new ClockwiseComparator from the specified center Vertex.
     *
     * @param center the center Vertex {@code v}
     */
    public ClockwiseComparator(final Vertex center) {
        this.center = center;
    }

    /**
     * Compares clock pointers {@code (v, a)} and {@code (v, c)}, where
     * {@code v} is the center Vertex of {@code this} comparator.
     * <p>
     * If clock pointers are directly opposite to one another, then {@code e1}
     * is considered to be strictly greater than {@code e2}.
     * <p>
     * Refer to {@link LocalRotations} interface definition for more info on
     * notation used.
     * <p>
     * <strong>Note:</strong> this comparator imposes orderings that are
     * inconsistent with equals.
     * <p>
     * {@inheritDoc}
     *
     * @param e1 DirectedEdge {@code (a, b)}
     * @param e2 DirectedEdge {@code (c, d)}
     */
    @Override
    public int compare(final DirectedEdge e1, final DirectedEdge e2) {
        Vector2 p1 = e1.from().position().sub(center.position());
        Vector2 p2 = e2.from().position().sub(center.position());
        int result = (int) Math.signum(p1.crs(p2));
        if (result == 0 && p1.hasOppositeDirection(p2)) {
            return 1;
        }
        return result;
    }
}
