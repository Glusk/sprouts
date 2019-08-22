package com.github.glusk2.sprouts.geom;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * A TrimmedPolyline is a Polyline without the first few points of
 * {@code original}.
 * <p>
 * The points to trim are determined by their distance from the zeroth point
 * in {@code original.points()}. The list of {@code original.points()} is
 * traversed until the first point, that is at least {@code mindDistance}
 * away from the zeroth point of {@code original.points()}, is found.
 * {@link #points()} returns a sub-list from this point to the end of
 * {@code original.points()}.
 * <p>
 * {@link #points()} may return an empty list if all of the points in
 * {@code original.points()} meet the trimming criteria.
 */
public final class TrimmedPolyline implements Polyline {
    /** The Polyline to trim. */
    private final Polyline original;
    /**
     * The distance criteria used to trim off excess points - refer to the
     * class definition Doc comment for more information.
     */
    private final float minDistance;

    /**
     * Constructs a new TrimmedPolyline.
     *
     * @param original the Polyline to trim
     * @param minDistance the distance criteria used to trim off excess points
     *                    - refer to the class definition Doc comment for more
     *                    information
     */
    public TrimmedPolyline(final Polyline original, final float minDistance) {
        this.original = original;
        this.minDistance = minDistance;
    }

    @Override
    public List<Vector2> points() {
        List<Vector2> points = new ArrayList<Vector2>(original.points());
        if (points.isEmpty()) {
            return new ArrayList<Vector2>();
        }
        Vector2 firstPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (firstPoint.dst(points.get(i)) >= minDistance) {
                return points.subList(i, points.size());
            }
        }
        return new ArrayList<Vector2>();
    }
}
