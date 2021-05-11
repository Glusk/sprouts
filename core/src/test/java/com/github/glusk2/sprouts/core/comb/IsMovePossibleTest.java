package com.github.glusk2.sprouts.core.comb;

import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

import org.junit.Test;

public final class IsMovePossibleTest {
    @Test
    public void moveNotPossibleInASimpleGraphWithNoLivingSporouts() {
        SproutsEdge e1 = new SproutsEdge(
            true,
            new Polyline.WrappedList(
                Vector2.Zero,
                new Vector2(1, 4),
                new Vector2(2, 3),
                new Vector2(4, 4)
            ),
            Color.BLACK, Color.BLACK);
        SproutsEdge e2 = new SproutsEdge(
            true,
            new Polyline.WrappedList(
                Vector2.Zero,
                new Vector2(1, 0),
                new Vector2(2, -1),
                new Vector2(4, 4)
            ),
            Color.BLACK, Color.BLACK);
        SproutsEdge e3 = new SproutsEdge(
            true,
            new Polyline.WrappedList(
                Vector2.Zero,
                new Vector2(-1, 0),
                new Vector2(-2, 10),
                new Vector2(4, 4)
            ),
            Color.BLACK, Color.BLACK);
        assertFalse(
            new IsMovePossible(
                () ->
                    new HashSet<>(
                        Arrays.asList(
                            e1,
                            e1.reversed(),
                            e2,
                            e2.reversed(),
                            e3,
                            e3.reversed()
                        )
                    )
            ).check()
        );
    }
}
