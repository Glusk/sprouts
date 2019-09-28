package com.github.glusk2.sprouts.core.comb;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * This class represents a Polyline-backed DirectedEdge.
 * <p>
 * The first and the last point of the {@code polyline} are elevated to graph
 * Vertices with it's designated colors - {@code fromColor}, {@code toColor} -
 * and rest of the points represent the connection between
 * the two in a 2-dimensional space.
 * <p>
 * If {@code polyline.points().size()} is equal to 1, then {@code from()}
 * may not equal {@code to()} when {@code fromColor.equals(toColor) == false}.
 * Such behavior is considered normal.
 */
public final class PolylineEdge implements DirectedEdge {
    /** The default edge color. */
    private static final Color DEFAULT_EDGE_COLOR = Color.BLACK;
    /** The color of the {@code from()} Vertex. */
    private final Color fromColor;
    /** The color of the {@code to()} Vertex. */
    private final Color toColor;
    /** The color of this DirectedEdge. */
    private final Color edgeColor;
    /** The polyline that backs this DirectedEdge. */
    private final Polyline polyline;

    /**
     * Constructs a new PolylineEdge by specifying the colors of the endpoint
     * Vertices and a list of {@code polylinePoints}, using the default edge
     * color.
     *
     * @param fromColor the color of the {@code from()} Vertex
     * @param toColor the color of the {@code to()} Vertex
     * @param polylinePoints a list of Polyline points that back this
     *                       DirectedEdge
     */
    public PolylineEdge(
        final Color fromColor,
        final Color toColor,
        final List<Vector2> polylinePoints
    ) {
        this(
            fromColor,
            toColor,
            DEFAULT_EDGE_COLOR,
            new Polyline.WrappedList(polylinePoints)
        );
    }

    /**
     * Constructs a new PolylineEdge by specifying the colors of the endpoint
     * Vertices and a list of {@code polylinePoints}, using the specified
     * {@code edgeColor}.
     *
     * @param fromColor the color of the {@code from()} Vertex
     * @param toColor the color of the {@code to()} Vertex
     * @param edgeColor the color of this DirectedEdge
     * @param polylinePoints a list of Polyline points that back this
     *                       DirectedEdge
     */
    public PolylineEdge(
        final Color fromColor,
        final Color toColor,
        final Color edgeColor,
        final List<Vector2> polylinePoints
    ) {
        this(
            fromColor,
            toColor,
            edgeColor,
            new Polyline.WrappedList(polylinePoints)
        );
    }

    /**
     * Constructs a new PolylineEdge by specifying the colors of the endpoint
     * Vertices and a {@code polyline}, using the specified {@code edgeColor}.
     *
     * @param fromColor the color of the {@code from()} Vertex
     * @param toColor the color of the {@code to()} Vertex
     * @param edgeColor the color of this DirectedEdge
     * @param polyline a Polyline that backs this DirectedEdge
     */
    public PolylineEdge(
        final Color fromColor,
        final Color toColor,
        final Color edgeColor,
        final Polyline polyline
    ) {
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.edgeColor = edgeColor;
        this.polyline = polyline;
    }

    @Override
    public Vertex from() {
        return
            new PresetVertex(
                fromColor,
                polyline.points().get(0),
                null
            );
    }

    @Override
    public Vertex to() {
        int lastPointIndex = polyline.points().size() - 1;
        return
            new PresetVertex(
                toColor,
                polyline.points().get(lastPointIndex),
                null
            );
    }

    @Override
    public Color color() {
        return edgeColor;
    }

    @Override
    public Polyline polyline() {
        return polyline;
    }

    @Override
    public int hashCode() {
        String hash = color().hashCode() + "-" + from().hashCode() + "-";
        Vector2[] points = polyline().points().toArray(new Vector2[0]);
        for (int i = 1; i < points.length - 1; i++) {
            hash += points[i].hashCode() + "-";
        }
        hash += to().hashCode();
        return hash.hashCode();
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
        boolean quickCheck =
            color().equals(that.color())
            && from().equals(that.from())
            && to().equals(that.to());
        return
            quickCheck
            && polyline().points().containsAll(that.polyline().points());
    }
}
