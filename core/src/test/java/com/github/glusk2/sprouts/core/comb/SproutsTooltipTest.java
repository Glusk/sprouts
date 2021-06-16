package com.github.glusk2.sprouts.core.comb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

import org.junit.Test;

public final class SproutsTooltipTest {
    @Test
    public void doNotHighlightOriginWithBlackDegreeOfTwo() {
        SproutsEdge e1 = new SproutsEdge(
            true,
            new Polyline.WrappedList(
                Vector2.Zero,
                new Vector2(-3, 2),
                new Vector2(-3, 6),
                new Vector2(0, 8)
            ), Color.BLACK, Color.BLACK
        );
        SproutsEdge e1Rev = e1.reversed();
        SproutsEdge e2 = new SproutsEdge(
            true,
            new Polyline.WrappedList(
                new Vector2(0, 8),
                new Vector2(3, 6),
                new Vector2(3, 2),
                Vector2.Zero
            ), Color.BLACK, Color.BLACK
        );
        SproutsEdge e2Rev = e2.reversed();

        assertFalse(
            new SproutsTooltip(
                // state
                () -> new HashSet<SproutsEdge>(
                    Arrays.asList(e1, e1Rev, e2, e2Rev)
                ),
                // face
                () -> new HashSet<SproutsEdge>(Arrays.asList(e1, e2)),
                new PresetVertex(Color.BLACK, Vector2.Zero)
            ).vertices().contains(
                new PresetVertex(Color.BLACK, Vector2.Zero)
            )
        );
    }
    @Test
    public void correctlyHighlightsSprout() {
        SproutsEdge e1 = new SproutsEdge(
            true,
            new Polyline.WrappedList(
                Vector2.Zero,
                new Vector2(-3, 2),
                new Vector2(-3, 6),
                new Vector2(0, 8)
            ), Color.BLACK, Color.BLACK
        );
        SproutsEdge e1Rev = e1.reversed();
        SproutsEdge e2 = new SproutsEdge(
            true,
            new Polyline.WrappedList(
                new Vector2(0, 8),
                new Vector2(3, 6),
                new Vector2(3, 2),
                Vector2.Zero
            ), Color.BLACK, Color.BLACK
        );
        SproutsEdge e2Rev = e2.reversed();

        assertTrue(
            new SproutsTooltip(
                // state
                () -> new HashSet<SproutsEdge>(
                    Arrays.asList(e1, e1Rev, e2, e2Rev)
                ),
                // face
                () -> new HashSet<SproutsEdge>(Arrays.asList(e1, e2)),
                new PresetVertex(Color.BLACK, new Vector2(0, 8))
            ).vertices().contains(
                new PresetVertex(Color.BLACK, Vector2.Zero)
            )
        );
    }
}
