package com.github.glusk2.sprouts.core.comb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

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
    @Test
    public void edgesWithSameDirectionAndSameOriginAreEqual() {
        assertEquals(
            new SproutsEdge(
                new Polyline.WrappedList(
                    Vector2.Zero, new Vector2(1, 0)
                ),
                Color.BLACK,
                Color.BLACK
            ),
            new SproutsEdge(
                true,
                new Polyline.WrappedList(
                    Vector2.Zero,
                    new Vector2(2, 0),
                    new Vector2(5, 5),
                    new Vector2(7, 7)
                ),
                Color.BLACK,
                Color.BLACK
            )
        );
    }
    @Test
    public void hashCollisionsCheck() {
        Vector2 tmp = new Vector2(1, 0);
        Set<Integer> set = new HashSet<>();
        for (float i = 0; i < 180f; i += .1f) {
            set.add(
                new SproutsEdge(
                    new Polyline.WrappedList(
                        Vector2.Zero, tmp.cpy()
                    ),
                    Color.BLACK,
                    Color.BLACK
                ).hashCode()
            );
            tmp.rotate(i);
        }
        assertThat(
            set.size(),
            is(1799)
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
