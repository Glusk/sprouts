package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * This class represents a check that tests whether the {@code point} lies on a
 * line segment.
 * <p>
 * <strong>Note:</strong> if the {@code point} lies on one of the segment
 * bounds, it is <em>not</em> considered part of the segment by {@code this}
 * check.
 */
public final class IsPointOnLineSegment {

    /**  The first endpoint of the line segment. */
    private final Vector2 p0;
    /**  The second endpoint of the line segment. */
    private final Vector2 p1;
    /**
     * {@code this} check tests whether {@code point} is on line segment
     * {@code p0-p1}.
     */
    private final Vector2 point;
    /**
     * The maximum distance between the line segment {@code p0-p1} and the
     * {@code point} at which {@code point} is still considered to lie on the
     * segment.
     */
    private final float maximumError;

    /**
     * Creates a new check that tests whether {@code point} lies on a line
     * segment delimited by {@code p0} and {@code p1}.
     * <p>
     * <strong>Note:</strong> if the {@code point} lies on one of the segment
     * bounds, it is <em>not</em> considered part of the segment by
     * {@code this} check.
     *
     * @param p0 the first endpoint of the line segment
     * @param p1 the second endpoint of the line segment
     * @param point this check tests whether {@code point} is on line segment
     *              {@code p0-p1}
     * @param maximumError the maximum distance between the line segment
     *                     {@code p0-p1} and the {@code point} at which
     *                     {@code point} is still considered to lie on the
     *                     segment
     */
    public IsPointOnLineSegment(
        final Vector2 p0,
        final Vector2 p1,
        final Vector2 point,
        final float maximumError
    ) {
        this.p0 = p0;
        this.p1 = p1;
        this.point = point;
        this.maximumError = maximumError;
    }

    /**
     * Returns a boolean evaluation of {@code this} check.
     * <p>
     * <strong>Note:</strong> if the {@code point} lies on one of the segment
     * bounds, it is <em>not</em> considered part of the segment by
     * {@code this} check.
     *
     * @return {@code true} if {@code point} is on line segment {@code p0-p1},
     *         {@code false} otherwise.
     */
    public boolean check() {
        boolean isPointOnLine =
            Intersector.distanceSegmentPoint(p0, p1, point) <= maximumError;
        Vector2 nearestSegmentPoint =
            Intersector.nearestSegmentPoint(p0, p1, point, new Vector2());
        return
            isPointOnLine
            && !(
                nearestSegmentPoint.epsilonEquals(p0)
             || nearestSegmentPoint.epsilonEquals(p1)
            );
    }
}
