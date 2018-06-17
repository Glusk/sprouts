package com.github.glusk2.sprouts.geom;

import org.la4j.inversion.GaussJordanInverter;
import org.la4j.matrix.DenseMatrix;
import org.la4j.matrix.SparseMatrix;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BSplineControlPoints implements Polyline<Point2D> {

    private static final double[] ONE_FOUR_ONE = {1, 4, 1};

    private final List<Point2D> sample;

    public BSplineControlPoints(List<Point2D> sample) {
        this.sample = sample;
    }

    @Override
    public List<Point2D> points() {
        Point2D[] s = sample.toArray(new Point2D[0]);
        int N = s.length;
        int n = N-1;
        if (n < 3) {
            return sample;
        }

        double[][] m = new double[n-1][n-1];
        double[][] c = new double[n-1][2];

        System.arraycopy(ONE_FOUR_ONE, 1, m[0], 0, 2);
        c[0][0] = 6 * s[1].getX() - s[0].getX();
        c[0][1] = 6 * s[1].getY() - s[0].getY();
        for (int i = 1; i < m.length - 1; i++) {
            System.arraycopy(ONE_FOUR_ONE, 0, m[i],i - 1,3);
            c[i][0] = 6 * s[i+1].getX();
            c[i][1] = 6 * s[i+1].getY();
        }
        System.arraycopy(ONE_FOUR_ONE, 0, m[n-2], n-3, 2);
        c[n-2][0] = 6 * s[n-1].getX() - s[n].getX();
        c[n-2][1] = 6 * s[n-1].getY() - s[n].getY();

        double[][] b =
            new GaussJordanInverter(
                SparseMatrix.from2DArray(m)
            ).inverse().multiply(
                DenseMatrix.from2DArray(c)
            ).toDenseMatrix().toArray();

        List<Point2D> controlPoints = new ArrayList<Point2D>(N);
        controlPoints.add(s[0]);
        for (double[] point : b) {
            controlPoints.add(new Point2D.Double(point[0], point[1]));
        }
        controlPoints.add(s[n]);
        return controlPoints;
    }
}
