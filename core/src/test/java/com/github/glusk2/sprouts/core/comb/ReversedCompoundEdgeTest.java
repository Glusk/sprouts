package com.github.glusk2.sprouts.core.comb;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

/** Test class for ReversedCompoundEdge. */
public final class ReversedCompoundEdgeTest {
    /**
     * Checks that a CompoundEdge with {@code origin} and a single point
     * {@code direction} edge is properly reversed.
     */
    @Test
    public void properlyReversesCompoundEdgeWithSinglePointDirection() {
        assertEquals(
            "CompoundEdge was not properly reversed!",
            new CompoundEdge.Wrapped(
                new PresetVertex(
                    new Vector2(1, 1)
                ),
                new StraightLineEdge(
                    new PresetVertex(
                        Vector2.Zero
                    )
                )
            ),
            new ReversedCompoundEdge(
                new PresetVertex(
                    Vector2.Zero
                ),
                new StraightLineEdge(
                    new PresetVertex(
                        new Vector2(1, 1)
                    )
                )
            )
        );
    }
}
