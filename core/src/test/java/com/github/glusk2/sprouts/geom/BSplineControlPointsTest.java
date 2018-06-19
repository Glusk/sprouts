package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

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
                actual.get(i).dst(expected.get(i)) < DELTA
            );
        }
    }
}