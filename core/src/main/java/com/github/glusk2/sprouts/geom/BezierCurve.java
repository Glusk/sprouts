package com.github.glusk2.sprouts.geom;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

/**
 * A {@code Curve} implementation that interpolates the sample using Bezier
 * splines.
 */
public final class BezierCurve implements Curve<Path<Vector2>> {
    /** Sample input points to interpolate. */
    private final Polyline sample;

    /**
     * B-spline control points that are used to define the "A-frames", which
     * define the glue points of Bezier splines in such a way that their first
     * and second derivatives match at the point of gluing.
     *
     * @see <a href="http://www.math.ucla.edu/%7Ebaker/149.1.02w/handouts/dd_splines.pdf">UCLA splines handout</a>
     */
    private final Polyline bSplineControlPoints;

    /**
     * Constructs a new {@code BezierCurve} by simplifying the {@code sample}
     * using the perpendicular distance simplification.
     * <p>
     * Equivalent to:
     * <pre>
     * new BezierCurve(
     *     newPerpDistSimpl(
     *         sample,
     *         tresholdPerpDistance
     *     )
     * )
     * </pre>
     *
     * @see #BezierCurve(Polyline)
     * @param sample a list of sample input points to interpolate
     * @param tresholdPerpDistance treshold delta for perpendicular distance
     *                             polyline simplification of the
     *                             {@code sample}
     */
    public BezierCurve(
        final List<Vector2> sample,
        final float tresholdPerpDistance
    ) {
        this(new PerpDistSimpl(sample, tresholdPerpDistance));
    }

    /**
     * Constructs a new {@code BezierCurve} by creating B-Spline control
     * points from {@code sample}.
     * <p>
     * Equivalent to:
     * <pre>
     * new BezierCurve(
     *     sample,
     *     new BSplineControlPoints(sample)
     * )
     * </pre>
     * @see #BezierCurve(Polyline, Polyline)
     * @param sample sample input points to interpolate
     */
    public BezierCurve(final Polyline sample) {
        this(
            sample,
            new BSplineControlPoints(sample)
        );
    }

    /**
     * Constructs a new {@code BezierCurve} from the specified {@code sample}
     * and {@code bSplineControlPoints}.
     *
     * @param sample sample input points to interpolate
     * @param bSplineControlPoints B-spline control points, used to help
     *                             interpolate the sample to a Bezier curve
     */
    public BezierCurve(
        final Polyline sample,
        final Polyline bSplineControlPoints
    ) {
        this.sample = sample;
        this.bSplineControlPoints = bSplineControlPoints;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:magicnumber")
    @Override
    public List<Path<Vector2>> splines() {
        List<Vector2> s = sample.points();
        List<Vector2> b = bSplineControlPoints.points();

        List<Path<Vector2>> splines = new ArrayList<Path<Vector2>>();
        for (int i = 1; i < s.size(); i++) {
            Vector2 dir = b.get(i).cpy().sub(b.get(i - 1));
            Vector2 p0 = s.get(i - 1).cpy();
            Vector2 p1 = b.get(i - 1).cpy().mulAdd(dir.cpy(), 1 / 3f);
            Vector2 p2 = b.get(i - 1).cpy().mulAdd(dir.cpy(), 2 / 3f);
            Vector2 p3 = s.get(i).cpy();
            splines.add((Path<Vector2>) new Bezier<Vector2>(p0, p1, p2, p3));
        }
        return splines;
    }
}
