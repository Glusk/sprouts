package com.github.glusk2.sprouts.geom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

/** A test class for {@code IsPointOnLineSegment}. */
public final class IsPointOnLineSegmentTest {
    /** Tests that a simple intersection is properly detected. */
    @Test
    public void detectsSimpleIntersection() {
        assertTrue(
            "Point falsely deemed not to be on the line segment!",
            new IsPointOnLineSegment(
                new Vector2(0, 0),
                new Vector2(2, 0),
                new Vector2(1, 0),
                0
            ).check()
        );
    }

    /**
     * Tests that a point that lies on the same line as the segment, but
     * outside of it, is <em>not</em> deemed to lie on the segment.
     */
    @Test
    public void detectsThatPointIsOnLineButNotOnTheSegment() {
        assertFalse(
            "Point falsely deemed to be on the line segment!",
            new IsPointOnLineSegment(
                new Vector2(0, 0),
                new Vector2(1, 0),
                new Vector2(2, 0),
                0
            ).check()
        );
    }
}
