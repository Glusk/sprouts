package com.github.glusk2.sprouts.core.comb;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

import org.junit.Test;

public final class DirectedEdgeTest {
    @Test
    public void oppositePointersAreNotNotEqual() {
        assertThat(
            new SproutsEdge(
                new Polyline.WrappedList(
                    Vector2.Zero, new Vector2(1, 0)
                ),
                Color.BLACK,
                Color.BLACK
            ).compareTo(
                new SproutsEdge(
                    new Polyline.WrappedList(
                        Vector2.Zero, new Vector2(-1, 0)
                    ),
                    Color.BLACK,
                    Color.BLACK
                )
            ),
            not(0)
        );
    }
    /* // YAGNI
    @Test
    public void createSimpleEdge() {
        new SproutsEdge(
            true,
            new Polyline.WrappedList(
                Vector2.Zero,
                new Vector2(1, 1),
                new Vector2(2, 2),
                new Vector2(3, 0),
                new Vector2(4, 0)
            ),
            Color.BLACK,
            Color.BLACK
        );
    }*/
}
