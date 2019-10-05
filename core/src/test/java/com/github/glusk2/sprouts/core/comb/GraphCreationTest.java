package com.github.glusk2.sprouts.core.comb;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

/** Test class for GraphCreation. */
public final class GraphCreationTest {
    /** The maximum rounding error permitted by this test class. */
    private static final float ROUNDING_ERROR = 1e-4f;

    /**
     * Tests that two points are added to the proper position on the
     * circle/clock: one at "three o'clock", the other at "nine o'clock".
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void addsTwoPoints() {
        Vertex[] vertices =
            new GraphCreation(
                2,
                10f,
                16,
                new Rectangle(0, 0, 400, 400)
            )
            .transformed()
            .vertices()
            .toArray(new Vertex[0]);

        Vector2 v1 = new Vector2(320, 200);
        Vector2 v2 = new Vector2(80, 200);

        assertTrue(
            "Sprouts are not positioned properly!",
            vertices[0].position().epsilonEquals(v1, ROUNDING_ERROR)
         && vertices[1].position().epsilonEquals(v2, ROUNDING_ERROR)
         || vertices[0].position().epsilonEquals(v2, ROUNDING_ERROR)
         && vertices[1].position().epsilonEquals(v1, ROUNDING_ERROR)
        );
    }
}
