package com.github.glusk2.sprouts.geom;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

/**
 * Polyline approximation of a curve.
 * <p>
 * A curve consists of one or more <em>splines</em>. Each spline can be divided
 * to a certain number of straight line segments. The greater the segment
 * countgit , the smoother the approximation.
 */
public final class CurveApproximation implements Polyline {

    /** Default number of segments per spline. */
    private static final int DEFAULT_SEGMENT_COUNT = 100;

    /** The curve to approximate as a polyline. */
    private final Curve<Path<Vector2>> curve;
    /** The number of segments per spline. */
    private final int numOfSegPerSpline;

    /**
     * Builds a new CurveApproximation with default number of segments per
     * spline.
     *
     * @param curve the curve to approximate as a polyline
     */
    public CurveApproximation(final Curve<Path<Vector2>> curve) {
        this(curve, DEFAULT_SEGMENT_COUNT);
    }

    /**
     * Builds a new CurveApproximation with the specified number of segments
     * per spline.
     *
     * @param curve the curve to approximate as a polyline
     * @param numOfSegPerSpline the number of segments per spline
     */
    public CurveApproximation(
        final Curve<Path<Vector2>> curve,
        final int numOfSegPerSpline
    ) {
        this.curve = curve;
        this.numOfSegPerSpline = numOfSegPerSpline;
    }

    @Override
    public List<Vector2> points() {
        List<Vector2> result = new ArrayList<Vector2>();
        Vector2 out = new Vector2();
        for (Path<Vector2> spline : curve.splines()) {
            for (int i = 0; i < numOfSegPerSpline; i++) {
                float t = 1f * i / numOfSegPerSpline;
                result.add(spline.valueAt(out, t).cpy());
            }
        }
        return result;
    }
}
