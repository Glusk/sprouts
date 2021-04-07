package com.github.glusk2.sprouts.core.comb;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * A directed edge from vertex {@code from()} to {@code to()}.
 * <p>
 * Every directed edge must have at least:
 * <ul>
 *   <li> 2 points, if {@code hasHooks} is set to {@code false},</li>
 *   <li> 4 points, if {@code hasHooks} is set to {@code true}</li>
 * </ul>
 * Furthermore, if {@code hasHooks} is set to true,
 * {@code intersectionPolyline()} returns a sub-list of the polyline passed
 * through the constructor without the endpoints (
 * {@code polyline.subList(1, polyline.size() - 1)}). This allows for a
 * slight margin of error when checking for intersections around the
 * vertices.
 * <p>
 * Edge direction is said to be positive if {@code from().position()} is the
 * first, and {@code to().position()} the last point in polyline.
 * <p>
 * Directed edges with the same origin ({@code from()}) can be compared. The
 * comparison is described in detail in {@link LocalRotations}.
 */
public final class SproutsEdge implements Comparable<SproutsEdge> {

    private final boolean direction;
    private final Polyline polyline;
    private final Color fromColor;
    private final Color toColor;
    private final Color edgeColor;
    private final boolean hasHooks;

    /**
     * Creates a red edge without hooks that has a positive direction.
     * <p>
     * This constructor is useful for creating cobweb edges.
     */
    public SproutsEdge(
        final Polyline polyline,
        final Color fromColor,
        final Color toColor
    ) {
        this(true, polyline, fromColor, toColor, Color.RED, false);
    }

    /**
     * Creates a black edge with hooks.
     * <p>
     * This constructor is useful for creating player sub-move edges.
     */
    public SproutsEdge(
        final boolean direction,
        final Polyline polyline,
        final Color fromColor,
        final Color toColor
    ) {
        this(direction, polyline, fromColor, toColor, Color.BLACK, true);
    }
    /**
     *
     * <p>
     * This is the generic primary constructor that's not meant to be used
     * directly.
     *
     * @param direction if {@code true} than this is an edge from
     *                    {@code polyline.get(0)} to
     *                    {@code polyline.get(polyline.size() - 1)}
     * @param polyline an unmodifiable list of points; can be passed as
     *                 {@code Collections.unmodifiableList(pointsList)}
     * @param fromColor
     * @param toColor
     * @param edgeColor
     * @param hasHooks
     */
    public SproutsEdge(
        final boolean direction,
        final Polyline polyline,
        final Color fromColor,
        final Color toColor,
        final Color edgeColor,
        final boolean hasHooks
    ) {
        /* // YAGNI
        if (!hasHooks && polyline.points().size() < 2) {
            throw new IllegalArgumentException(
                "An edge without hooks must have at least 2 points"
            );
        }
        if (hasHooks && polyline.points().size() < 4) {
            throw new IllegalArgumentException(
                "An edge with hooks must have at least 4 points"
            );
        }
        */
        this.direction = direction;
        this.polyline = polyline;
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.edgeColor = edgeColor;
        this.hasHooks = hasHooks;
    }

    public Vertex from() {
        List<Vector2> points = polyline.points();
        if (direction) {
            return new PresetVertex(fromColor, points.get(0));
        }
        return new PresetVertex(fromColor, points.get(points.size() - 1));
    }

    public Vertex to() {
        List<Vector2> points = polyline.points();
        if (direction) {
            return new PresetVertex(toColor, points.get(points.size() - 1));
        }
        return new PresetVertex(toColor, points.get(0));
    }

    public Color color() {
        return edgeColor;
    }

    public Polyline polyline() {
        return this.polyline;
    }

    public Polyline intersectionPolyline() {
        if (hasHooks) {
            List<Vector2> points = polyline.points();
            return
                new Polyline.WrappedList(
                    points.subList(1, points.size() - 1)
                );
        }
        return polyline;
    }

    public SproutsEdge reversed() {
        return
            new SproutsEdge(
                !direction,
                polyline,
                toColor,
                fromColor,
                edgeColor,
                hasHooks
            );
    }

    private Vector2 secondPointInPositiveDirection() {
        List<Vector2> points = polyline.points();
        if (direction) {
            return points.get(1);
        }
        return points.get(points.size() - 2);
    }

    @Override
    public int compareTo(final SproutsEdge that) {
        Vector2 v = from().position();
        Vector2 a = secondPointInPositiveDirection();

        Vector2 vT = that.from().position();
        Vector2 aT = that.secondPointInPositiveDirection();

        if (vT.equals(v)) {
            Vector2 p1 = a.cpy().sub(v);
            Vector2 p2 = aT.cpy().sub(v);
            int result = (int) Math.signum(p1.crs(p2));
            if (result == 0 && p1.hasOppositeDirection(p2)) {
                return 1;
            }
            return result;
        }
        throw new IllegalArgumentException(
            "You can only compare edges with the same origin (from())."
        );
    }

    @Override
    public int hashCode() {
        Vector2 v = from().position();
        Vector2 a = secondPointInPositiveDirection();

        Vector2 p1 = a.cpy().sub(v);
        // normalize the vector; use squared length for performance
        p1.scl(1f / p1.len2());

        int h1 = from().hashCode();
        int h2 = PresetVertex.vector2HashCode(p1);
        return h1 ^ ((h2 >>> 16) | (h2 << 16));
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || ! (o instanceof SproutsEdge)) {
            return false;
        }
        SproutsEdge that = (SproutsEdge) o;
        return this.from().equals(that.from()) && this.compareTo(that) == 0;
    }
}
