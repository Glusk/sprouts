package com.github.glusk2.sprouts.core.geom;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@code BSplineControlPoints}.
 *
 * @see com.github.glusk2.sprouts.core.geom.BSplineControlPoints
 */
public final class BSplineControlPointsTest {
    /**
     * A test case from this paper:
     * <a href="http://www.math.ucla.edu/%7Ebaker/149.1.02w/handouts/dd_splines.pdf">UCLA splines handout</a>.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void testUclaPaper() {
        List<Vector2> actual = new BSplineControlPoints(
            Arrays.<Vector2>asList(
                new Vector2(1, -1),
                new Vector2(-1, 2),
                new Vector2(1, 4),
                new Vector2(4, 3),
                new Vector2(7, 5)
            )
        ).points();

        List<Vector2> expected =
            Arrays.<Vector2>asList(
                new Vector2(1, -1),
                new Vector2(-2, 2),
                new Vector2(1, 5),
                new Vector2(4, 2),
                new Vector2(7, 5)
            );

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertTrue(
                "Computed point is too far off!",
                actual.get(i).epsilonEquals(expected.get(i))
            );
        }
    }

    /**
     * Tests for valid B-Spline control points if sample size is equal to 3.
     * <p>
     * The matrix system of linear equations breaks on this case and control
     * points have to be computed like this (pseudocode):
     * <pre>
     * // s ... sample points
     * // b ... B-spline control points
     * b[0] = s[0];
     * b[1] = (6*s[1] - s[0] - s[2]) / 4;
     * b[2] = s[2];
     * </pre>
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void worksWithThreeSamplePoints() {
        List<Vector2> actual = new BSplineControlPoints(
            Arrays.<Vector2>asList(
                new Vector2(0, 0),
                new Vector2(1.5f, 3f),
                new Vector2(4, 4)
            )
        ).points();

        List<Vector2> expected =
            Arrays.<Vector2>asList(
                new Vector2(0, 0),
                new Vector2(1.25f, 3.5f),
                new Vector2(4, 4)
            );

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertTrue(
                "Computed point is too far off!",
                actual.get(i).epsilonEquals(expected.get(i))
            );
        }
    }
}
