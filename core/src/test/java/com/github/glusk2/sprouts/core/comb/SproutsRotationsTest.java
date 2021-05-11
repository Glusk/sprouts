package com.github.glusk2.sprouts.core.comb;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

import org.junit.Test;

public final class SproutsRotationsTest {
    @Test
    public void checksClockwiseEdgeOrder() {
        SproutsEdge e0 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(2, 2)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge e1 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(0, 3)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge e2 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(-4, 1)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge e3 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(-1, 2)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge e4 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(3, -1)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge e5 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(-1, -2)),
            Color.BLACK, Color.BLACK
        );

        SproutsRotations lr = new SproutsRotations(e0, e1, e2, e3, e4, e5);
        SproutsEdge[] result = new SproutsEdge[] {
            e1, e0, e4, e5, e2, e3
        };
        for (int i = 0; i < result.length; i++) {
            int j = (i + 1) % result.length;
            assertEquals(result[j], lr.next(result[i]));
        }
    }
}
