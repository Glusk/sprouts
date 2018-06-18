package com.github.glusk2.sprouts.geom;

import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BSplineControlPointsTest {
    private static final double DELTA = 1E-10;
    /**
     * A test case from this paper;
     * http://www.math.ucla.edu/%7Ebaker/149.1.02w/handouts/dd_splines.pdf
     */
    @Test
    public void testUclaPaper() {
        List<Point2D> actual = new BSplineControlPoints(
            Arrays.<Point2D>asList(
                new Point2D.Double(1, -1),
                new Point2D.Double(-1, 2),
                new Point2D.Double(1, 4),
                new Point2D.Double(4, 3),
                new Point2D.Double(7, 5)
            )
        ).points();

        List<Point2D> expected =
            Arrays.<Point2D>asList(
                new Point2D.Double(1, -1),
                new Point2D.Double(-2, 2),
                new Point2D.Double(1, 5),
                new Point2D.Double(4, 2),
                new Point2D.Double(7, 5)
            );

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertTrue(
                "Computed point is too far off!",
                actual.get(i).distance(expected.get(i)) < DELTA
            );
        }
    }
}