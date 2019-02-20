package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * A perpendicular distance polyline simplification.
 *
 * @see <a href="http://psimpl.sourceforge.net/perpendicular-distance.html">psimpl - Perpendicular Distance</a>
 */
public final class PerpDistSimpl implements Polyline {

    /**
     * If {@code original.points().size()} is smaller than
     * {@code MIN_POLYLINE_SIZE} then we don't run the simplification
     * algorithm.
     */
    private static final int MIN_POLYLINE_SIZE = 3;

    /** The polyline to simplify. */
    private final Polyline original;
    /** Point-to-segment distance tolerance. */
    private final double tolerance;

    /**
     * Creates a new {@code Polyline} that simplifies the {@code original}
     * list of points using perpendicular distance polyline simplification.
     * <p>
     * Equivalent to:
     * <pre>
     * new PerpDistSimpl(
     *     new WrappedList(original),
     *     tolerance
     * )
     * </pre>
     *
     * @see #PerpDistSimpl(Polyline, double)
     * @param original list of points representing the polyline to simplify
     * @param tolerance point-to-segment distance tolerance
     */
    public PerpDistSimpl(
        final List<Vector2> original,
        final double tolerance
    ) {
        this(new WrappedList(original), tolerance);
    }

    /**
     * Creates a new {@code Polyline} that simplifies the {@code original}
     * polyline using perpendicular distance polyline simplification.
     *
     * @param original the polyline to simplify
     * @param tolerance point-to-segment distance tolerance
     */
    public PerpDistSimpl(final Polyline original, final double tolerance) {
        this.original = original;
        this.tolerance = tolerance;
    }

    /**
     * Computes the perpendicular distance ({@code e}) between point {@code x}
     * and the line segment {@code p0-p1}.
     * <p>
     * <pre>
     *          * x
     *         /|
     *        / |
     *   a   /  | e
     *      /   |
     *     /    |
     *    /_____|_____
     *   *   p        *
     *          b
     *   p0           p1
     * </pre>
     *
     * @param p0 line segment end point
     * @param p1 line segment end point
     * @param x the point to compute the distance to
     * @return {@code e} - the perpendicular distance between the line segment
     *         {@code p0-p1} and point {@code x}
     */
    private static double perpDistance(
        final Vector2 p0,
        final Vector2 p1,
        final Vector2 x
    ) {
        Vector2 a = x.cpy().sub(p0);
        Vector2 b = p1.cpy().sub(p0);
        Vector2 p = b.scl(a.dot(b) / b.dot(b));
        Vector2 e = a.sub(p);
        return e.len();
    }

    /** {@inheritDoc} */
    @Override
    public List<Vector2> points() {
        List<Vector2> originalPoints = original.points();
        if (originalPoints.size() < MIN_POLYLINE_SIZE) {
            return originalPoints;
        }
        List<Vector2> simplified = new ArrayList<Vector2>();
        for (int i = 0; i < originalPoints.size() - 2; i++) {
            simplified.add(originalPoints.get(i));

            Vector2 p0 = originalPoints.get(i);
            Vector2 p1 = originalPoints.get(i + 2);
            Vector2 x = originalPoints.get(i + 1);
            if (perpDistance(p0, p1, x) < tolerance) {
                i++;
            }
        }
        simplified.add(originalPoints.get(originalPoints.size() - 1));
        return simplified;
    }
}
