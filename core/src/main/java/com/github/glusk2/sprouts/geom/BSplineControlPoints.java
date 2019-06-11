package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A B-Spline control points polyline.
 * <p>
 * Such a polyline is used as a frame to produce a {@code BezierCurve}.
 *
 * @see com.github.glusk2.sprouts.geom.BezierCurve
 * @see <a href="http://www.math.ucla.edu/%7Ebaker/149.1.02w/handouts/dd_splines.pdf">UCLA splines handout</a>
 */
public final class BSplineControlPoints implements Polyline {

    /**
     * Special coefficients used to produce the coefficient vectors of the
     * "1 4 1 matrix".
     *
     * @see <a href="http://www.math.ucla.edu/%7Ebaker/149.1.02w/handouts/dd_splines.pdf">UCLA splines handout</a>
     */
    private static final int[] SPECIAL_COEFFICIENTS = {1, 4, 1};

    /**
     * The minimal sample size.
     * <p>
     * If {@code sample.points().size() < MIN_SAMPLE_SIZE} then B-Spline
     * control points are not computed. {@code sample.points()} gets returned
     * instead.
     */
    private static final int MIN_SAMPLE_SIZE = 3;

    /** The sample polyline used to produce B-Spline control points. */
    private final Polyline sample;

    /**
     * Creates new {@code BSplineControlPoints} from a list of sample points.
     * <p>
     * Equivalent to:
     * <pre>
     * new BSplineControlPoints(
     *     new Polyline.WrappedList(sample)
     * )
     * </pre>
     *
     * @param sample the list of sample points
     */
    public BSplineControlPoints(final List<Vector2> sample) {
        this(new Polyline.WrappedList(sample));
    }

    /**
     * Creates new {@code BSplineControlPoints} from the sample polyline.
     *
     * @param sample the sample polyline
     */
    public BSplineControlPoints(final Polyline sample) {
        this.sample = sample;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:magicnumber")
    @Override
    public List<Vector2> points() {
        Vector2[] s = sample.points().toArray(new Vector2[0]);
        if (s.length < MIN_SAMPLE_SIZE) {
            return sample.points();
        }
        if (s.length == MIN_SAMPLE_SIZE) {
            return Arrays.asList(
                s[0].cpy(),
                s[1].cpy().scl(6f).sub(s[0]).sub(s[2]).scl(.25f),
                s[2].cpy()
            );
        }
        int n = s.length - 1;

        double[] cX = new double[n - 1];
        double[] cY = new double[n - 1];

        cX[0] = 6 * s[1].x - s[0].x;
        cY[0] = 6 * s[1].y - s[0].y;
        for (int i = 1; i < n - 2; i++) {
            cX[i] = 6 * s[i + 1].x;
            cY[i] = 6 * s[i + 1].y;
        }
        cX[n - 2] = 6 * s[n - 1].x - s[n].x;
        cY[n - 2] = 6 * s[n - 1].y - s[n].y;

        double[] bX = tridiagonalPreset(n - 1, cX);
        double[] bY = tridiagonalPreset(n - 1, cY);

        List<Vector2> controlPoints = new ArrayList<Vector2>(s.length);
        controlPoints.add(s[0]);
        for (int i = 0; i < n - 1; i++) {
            controlPoints.add(
                new Vector2(
                    (float) bX[i],
                    (float) bY[i]
                )
            );
        }
        controlPoints.add(s[n]);
        return controlPoints;
    }


    /**
     * Prepares special vectors (a, b, c) and calls
     * {@link
     *  #tridiagonalAlgorithm(int, double[], double[], double[], double[])}.
     *
     * @param n dimension of the tridiagonal system
     * @param d right-hand side of the equation; length: {@code n}
     * @return a vector of solutions: {@code x}; length: {@code n}
     */
    private static double[] tridiagonalPreset(final int n, final double[] d) {
        double[] a = new double[n - 1];
        double[] b = new double[n];
        double[] c = new double[n - 1];

        Arrays.fill(a, SPECIAL_COEFFICIENTS[0]);
        Arrays.fill(b, SPECIAL_COEFFICIENTS[1]);
        Arrays.fill(c, SPECIAL_COEFFICIENTS[2]);

        return tridiagonalAlgorithm(n, a, b, c, d);
    }

    /**
     * Solves a tridiagonal system of linear equations by using the Thomas
     * Algorithm.
     * <p>
     * <pre>
     *|b0 c0         0| |x0  |   |d0  |
     *|a1 b1 c1       | |x1  |   |d1  |
     *|  a2 b2 c2     | |x2  |   |d2  |
     *|    . . .      | | .  | = | .  |
     *|               | | .  |   | .  |
     *|           cn-2| | .  |   | .  |
     *|0    an-1  bn-1| |xn-1|   |dn-1|
     * </pre>
     *
     * @param n dimension of the coefficient matrix
     * @param a coefficients under the diagonal; length: {@code n-1}
     * @param b coefficients on the diagonal; length: {@code n}
     * @param c coefficients over the diagonal; length: {@code n-1}
     * @param d right-hand side of the equation; length: {@code n}
     * @return a vector of solutions: {@code x}; length: {@code n}
     */
    private static double[] tridiagonalAlgorithm(
        final int n,
        final double[] a,
        final double[] b,
        final double[] c,
        final double[] d
    ) {
        double[] newC = new double[c.length];
        double[] newD = new double[d.length];
        newC[0] = c[0] / b[0];
        newD[0] = d[0] / b[0];
        for (int i = 1; i < n; i++) {
            double denominator = b[i] - a[i - 1] * newC[i - 1];
            if (i != n - 1) {
                newC[i] = c[i] / denominator;
            }
            newD[i] = (d[i] - a[i - 1] * newD[i - 1]) / denominator;
        }
        double[] x = new double[n];
        x[n - 1] = newD[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            x[i] = newD[i] - newC[i] * x[i + 1];
        }
        return x;
    }
}
