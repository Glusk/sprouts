package com.github.glusk2.sprouts.comb;

import java.util.Comparator;

import com.badlogic.gdx.math.Vector2;

/**
 * Comparator that orders vertices in clock-wise order, given a center point.
 * <p>
 * The order is determined by computing the cross product of vectors:
 * <ul>
 *   <li>{@code center-v1}</li>
 *   <li>{@code center-v2}</li>
 * </ul>
 *
 * @see <a href ="https://stackoverflow.com/a/6989383"></a>
 */
public final class ClockwiseComparator implements Comparator<Vertex> {
    /** Center point position. */
    private final Vector2 centerPoint;

    /**
     * Constructs a new {@code RadialComparator} from the specified Vertex
     * center point.
     *
     * @param centerPoint center point vertex
     */
    public ClockwiseComparator(final Vertex centerPoint) {
        this(centerPoint.position());
    }

    /**
     * Constructs a new {@code RadialComparator} from the specified central
     * position.
     *
     * @param centerPoint center point position
     */
    public ClockwiseComparator(final Vector2 centerPoint) {
        this.centerPoint = centerPoint;
    }

    /**
     * If clock pointers overlap, they are considered equal regardless of
     * their distance from the centre.
     * <p>
     * If clock pointers are directly opposite to one another, then {@code v1}
     * is considered as strictly greater than {@code v2}.
     * <p>
     * <strong>Note:</strong> this comparator imposes orderings that are
     * inconsistent with equals.
     * <p>
     * {@inheritDoc}
     *
     * @param v1 vertex to compare against {@code v2}
     * @param v2 vertex to compare against {@code v1}
     */
    @Override
    public int compare(final Vertex v1, final Vertex v2) {
        Vector2 cPv1 = v1.position().sub(centerPoint);
        Vector2 cPv2 = v2.position().sub(centerPoint);
        int result = (int) Math.signum(cPv1.crs(cPv2));
        if (result == 0 && cPv1.hasOppositeDirection(cPv2)) {
            return 1;
        }
        return result;
    }
}
