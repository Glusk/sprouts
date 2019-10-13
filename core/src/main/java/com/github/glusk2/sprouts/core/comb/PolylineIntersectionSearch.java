package com.github.glusk2.sprouts.core.comb;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * Objects of this class can find the intersection between a line segment and
 * a Polyline.
 * <p>
 * The {@code result()} is returned as a Vertex with the provided
 * {@code intersectionColor}.
 */
public final class PolylineIntersectionSearch implements VertexSearch {
    /** The first line segment boundary. */
    private final Vector2 p0;
    /** The second line segment boundary. */
    private final Vector2 p1;
    /**
     * The Polyline to check for the intersection with the {@code p0-p1} line
     * segment.
     */
    private final Polyline polyline;
    /**
     * The Color of the Vertex returned by {@code result()}, if there is an
     * intersection.
     */
    private final Color intersectionColor;

    /**
     * Creates a new PolylineIntersectionSearch object, by providing the line
     * segment, {@code polyline} and {@code intersectionColor}.
     *
     * @param p0 the first line segment boundary
     * @param p1 the second line segment boundary
     * @param polyline the Polyline to check for the intersection with the
     *                 {@code p0-p1} line segment
     * @param intersectionColor the Color of the Vertex returned by
     *                          {@code result()}, if there is an intersection
     */
    public PolylineIntersectionSearch(
        final Vector2 p0,
        final Vector2 p1,
        final Polyline polyline,
        final Color intersectionColor
    ) {
        this.p0 = p0;
        this.p1 = p1;
        this.polyline = polyline;
        this.intersectionColor = intersectionColor;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result is the intersection point between line segment {@code p0-p1}
     * and {@code polyline}.
     * <p>
     * The {@code color()} of the Vertex returned is equal to
     * {@code intersectionColor}.
     * <p>
     * If line segment {@code p0-p1} is simply connected to {@code polyline}
     * through one of its bounds, there is no intersection.
     *
     * @return the intersection Vertex between line segment {@code p0-p1} and
     *         {@code polyline}; if the intersection is not found, and new
     *         instance of {@link VoidVertex} is returned
     */
    @Override
    public Vertex result() {
        List<Vector2> points = polyline.points();
        Vector2 intersection = new Vector2();
        for (int i = 1; i < points.size(); i++) {
            boolean intersects =
                Intersector.intersectSegments(
                    p0,
                    p1,
                    points.get(i - 1),
                    points.get(i),
                    intersection
                );
            if (
                intersects
             && !p0.epsilonEquals(intersection)
             && !p1.epsilonEquals(intersection)
            ) {
                return
                    new PresetVertex(
                        intersectionColor,
                        intersection,
                        (String) null
                    );
            }
        }
        return new VoidVertex();
    }
}
