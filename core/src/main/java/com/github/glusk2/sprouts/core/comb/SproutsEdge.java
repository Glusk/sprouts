package com.github.glusk2.sprouts.core.comb;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * A directed edge from vertex {@code from()} to {@code to()}.
 * <p>
 * Every directed edge must have at least 2 points.
 * <p>
 * Edge direction can be either positive or negative. Refer to
 * {@link #isPositive()} for more info.
 * <p>
 * Directed edges with the same origin ({@code from()}) can be compared.
 */
public final class SproutsEdge implements Comparable<SproutsEdge> {
    /**
     * The direction of this edge ({@code true} - positive, {@code false} -
     * negative).
     */
    private final boolean direction;
    /** The polyline that represents this edge. */
    private final Polyline polyline;
    /** The color of the origin vertex ({@code this.from()}). */
    private final Color fromColor;
    /** The color of the destination vertex ({@code this.to()}). */
    private final Color toColor;
    /** The color of this edge. */
    private final Color edgeColor;

    /**
     * Creates a red edge has a positive direction.
     * <p>
     * This constructor is useful for creating cobweb edges.
     *
     * @param polyline the polyline that represents this edge
     * @param fromColor the color of the origin vertex ({@code this.from()})
     * @param toColor the color of the destination vertex ({@code this.to()})
     */
    public SproutsEdge(
        final Polyline polyline,
        final Color fromColor,
        final Color toColor
    ) {
        this(true, polyline, fromColor, toColor, Color.RED);
    }

    /**
     * Creates a black edge.
     * <p>
     * This constructor is useful for creating player sub-move edges.
     *
     * @param direction the direction of this edge ({@code true} - positive,
     *                  {@code false} - negative)
     * @param polyline the polyline that represents this edge
     * @param fromColor the color of the origin vertex ({@code this.from()})
     * @param toColor the color of the destination vertex ({@code this.to()})
     */
    public SproutsEdge(
        final boolean direction,
        final Polyline polyline,
        final Color fromColor,
        final Color toColor
    ) {
        this(direction, polyline, fromColor, toColor, Color.BLACK);
    }
    /**
     * Creates a new edge from a full list of attributes.
     * <p>
     * This is the generic primary constructor that's not meant to be used
     * directly.
     *
     * @param direction the direction of this edge ({@code true} - positive,
     *                  {@code false} - negative)
     * @param polyline the polyline that represents this edge
     * @param fromColor the color of the origin vertex ({@code this.from()})
     * @param toColor the color of the destination vertex ({@code this.to()})
     * @param edgeColor the color of this edge
     */
    public SproutsEdge(
        final boolean direction,
        final Polyline polyline,
        final Color fromColor,
        final Color toColor,
        final Color edgeColor
    ) {
        if (polyline.points().size() < 2) {
            throw new IllegalArgumentException(
                "An edge must have at least 2 points"
            );
        }
        this.direction = direction;
        this.polyline = polyline;
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.edgeColor = edgeColor;
    }

    /**
     * Returns the <em>origin</em> of this edge.
     *
     * @return a new vertex that represents the origin of this directed edge
     */
    public Vertex from() {
        List<Vector2> points = polyline.points();
        if (direction) {
            return new PresetVertex(fromColor, points.get(0));
        }
        return new PresetVertex(fromColor, points.get(points.size() - 1));
    }

    /**
     * Returns the <em>destination</em> of this edge.
     *
     * @return a new vertex that represents the destination of this directed
     *         edge
     */
    public Vertex to() {
        List<Vector2> points = polyline.points();
        if (direction) {
            return new PresetVertex(toColor, points.get(points.size() - 1));
        }
        return new PresetVertex(toColor, points.get(0));
    }

    /**
     * Returns the color of this edge.
     *
     * @return the color of this edge
     */
    public Color color() {
        return edgeColor;
    }

    /**
     * Returns the polyline that represents this edge.
     *
     * @return the polyline that represents this edge
     */
    public Polyline polyline() {
        return this.polyline;
    }

    /**
     * Reverses this edge and returns the result as a new edge.
     *
     * @return a new edge that is the reverse of {@code this}
     */
    public SproutsEdge reversed() {
        return
            new SproutsEdge(
                !direction,
                polyline,
                toColor,
                fromColor,
                edgeColor
            );
    }

    /**
     * Return the direction of this polyline.
     * <p>
     * A <strong>positive</strong> direction means that {@code this} is a
     * directed edge of the form:
     * <pre>
     * p[0], p[1], ..., p[polylineSize-1]
     * </pre>
     * and a <strong>negative</strong> direction means that {@code this} is a
     * directed edge of the form:
     * <pre>
     * p[polylineSize-1], p[polylineSize-2], ..., p[0]
     * </pre>
     * where {@code p[i]} are the points in {@code this.polyline().points()}.
     *
     * @return {@code true} - positive, {@code false} - negative
     */
    public boolean isPositive() {
        return this.direction;
    }

    /**
     * Returns the second point of the polyline (that represents this edge)
     * in the positive direction.
     * <p>
     * This method is useful for creating "hooks" of the form:
     * <pre>
     * [this.from().position(), p2]
     * </pre>
     * where {@code p2} is the result of this method.
     * <p>
     * Such "hooks" can be used to establish the order of edges around a
     * common origin ({@link SproutsEdge#from()}).
     *
     * @return the position of the second point in the positive direction
     */
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
        return h1 ^ ((h2 >>> Short.SIZE) | (h2 << Short.SIZE));
    }
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof SproutsEdge)) {
            return false;
        }
        SproutsEdge that = (SproutsEdge) o;
        return this.from().equals(that.from()) && this.compareTo(that) == 0;
    }
}
