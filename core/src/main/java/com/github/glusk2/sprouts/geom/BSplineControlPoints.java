package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Vector2;
import org.la4j.inversion.GaussJordanInverter;
import org.la4j.matrix.DenseMatrix;
import org.la4j.matrix.SparseMatrix;

import java.util.ArrayList;
import java.util.List;

public class BSplineControlPoints implements Polyline {

    private static final double[] ONE_FOUR_ONE = {1, 4, 1};

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

        double[][] m = new double[n-1][n-1];
        double[][] c = new double[n-1][2];

        System.arraycopy(ONE_FOUR_ONE, 1, m[0], 0, 2);
        c[0][0] = 6 * s[1].x - s[0].x;
        c[0][1] = 6 * s[1].y - s[0].y;
        for (int i = 1; i < m.length - 1; i++) {
            System.arraycopy(ONE_FOUR_ONE, 0, m[i],i - 1,3);
            c[i][0] = 6 * s[i+1].x;
            c[i][1] = 6 * s[i+1].y;
        }
        System.arraycopy(ONE_FOUR_ONE, 0, m[n-2], n-3, 2);
        c[n-2][0] = 6 * s[n-1].x - s[n].x;
        c[n-2][1] = 6 * s[n-1].y - s[n].y;

        double[][] b =
            new GaussJordanInverter(
                SparseMatrix.from2DArray(m)
            ).inverse().multiply(
                DenseMatrix.from2DArray(c)
            ).toDenseMatrix().toArray();

        List<Vector2> controlPoints = new ArrayList<Vector2>(N);
        controlPoints.add(s[0]);
        for (double[] point : b) {
            controlPoints.add(
                new Vector2(
                    (float) point[0],
                    (float) point[1]
                )
            );
        }
        controlPoints.add(s[n]);
        return controlPoints;
    }
}
