package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BSplineControlPoints implements Polyline {

    private final Polyline sample;

    public BSplineControlPoints(List<Vector2> sample) {
        this(new Polyline.SAMPLE_WRAPPER(sample));
    }

    public BSplineControlPoints(Polyline sample) {
        this.sample = sample;
    }

    @Override
    public List<Vector2> points() {
        Vector2[] s = sample.points().toArray(new Vector2[0]);
        int N = s.length;
        int n = N-1;
        if (n < 3) {
            return sample.points();
        }

        double[] cX = new double[n-1];
        double[] cY = new double[n-1];

        cX[0] = 6 * s[1].x - s[0].x;
        cY[0] = 6 * s[1].y - s[0].y;
        for (int i = 1; i < n-2; i++) {
            cX[i] = 6 * s[i+1].x;
            cY[i] = 6 * s[i+1].y;
        }
        cX[n-2] = 6 * s[n-1].x - s[n].x;
        cY[n-2] = 6 * s[n-1].y - s[n].y;

        double[] bX = tridiagonalPreset(n-1, cX);
        double[] bY = tridiagonalPreset(n-1, cY);

        List<Vector2> controlPoints = new ArrayList<Vector2>(N);
        controlPoints.add(s[0]);
        for (int i = 0; i < n-1; i++) {
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
     * {@link #tridiagonalAlgorithm(int, double[], double[], double[], double[])}.
     *
     * @param n dimension of the tridiagonal system
     * @param d right-hand side of the equation; length: {@code n}
     */
    private static double[] tridiagonalPreset(int n, double[] d) {
        double[] a = new double[n-1];
        double[] b = new double[n];
        double[] c = new double[n-1];

        Arrays.fill(a, 1.0);
        Arrays.fill(b, 4.0);
        Arrays.fill(c, 1.0);

        return tridiagonalAlgorithm(n, a, b, c, d);
    }

    /**
     * Solves a tridiagonal system of linear equations by using the Thomas
     * Algorithm.
     * <p>
     * <pre>
     *
     *   |b0 c0         0| |x0  |   |d0  |
     *   |a1 b1 c1       | |x1  |   |d1  |
     *   |  a2 b2 c2     | |x2  |   |d2  |
     *   |    . . .      | | .  | = | .  |
     *   |               | | .  |   | .  |
     *   |           cn-2| | .  |   | .  |
     *   |0    an-1  bn-1| |xn-1|   |dn-1|
     * </pre>
     *
     * @param n dimension of the coefficient matrix
     * @param a coefficients under the diagonal; length: {@code n-1}
     * @param b coefficients on the diagonal; length: {@code n}
     * @param c coefficients over the diagonal; length: {@code n-1}
     * @param d right-hand side of the equation; length: {@code n}
     * @return a vector of solutions: {@code x}: length: {@code n}
     */
    private static double[] tridiagonalAlgorithm(
        int n,
        double[] a,
        double[] b,
        double[] c,
        double[] d
    ) {
        double [] newC = new double[c.length+1];
        double [] newD = new double[d.length];
        newC[0] = c[0] / b[0];
        newD[0] = d[0] / b[0];
        for (int i = 1; i < n; i++) {
            if (i != n-1) {
                newC[i] = c[i] / (b[i] - a[i - 1] * newC[i - 1]);
            }
            newD[i] = (d[i] - a[i-1] * newD[i-1]) / (b[i] - a[i-1] * newC[i-1]);
        }
        double[] x = new double[n];
        x[n-1] = newD[n-1];
        for (int i = n-2; i >= 0; i--) {
            x[i] = newD[i] - newC[i] * x[i+1];
        }
        return x;
    }
}
