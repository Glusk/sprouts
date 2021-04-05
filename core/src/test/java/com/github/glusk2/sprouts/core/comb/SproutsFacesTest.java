package com.github.glusk2.sprouts.core.comb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

import org.junit.Test;

public final class SproutsFacesTest {
    @Test
    public void buildsCorrectFaces() {
        SproutsEdge v1v4 = new SproutsEdge(
            new Polyline.WrappedList(new Vector2(4, 0), new Vector2(2, 0)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge v4v1 = v1v4.reversed();
        SproutsEdge v1v9 = new SproutsEdge(
            new Polyline.WrappedList(new Vector2(4, 0), new Vector2(6, 0)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge v9v1 = v1v9.reversed();
        SproutsEdge v2v4 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(2, 0)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge v4v2 = v2v4.reversed();
        SproutsEdge v2v6 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(0, -2)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge v6v2 = v2v6.reversed();
        SproutsEdge v2v7 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(0, 2)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge v7v2 = v2v7.reversed();
        SproutsEdge v4v5 = new SproutsEdge(
            new Polyline.WrappedList(new Vector2(2, 0), new Vector2(1, -1)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge v5v4 = v4v5.reversed();
        SproutsEdge v5v6 = new SproutsEdge(
            new Polyline.WrappedList(new Vector2(1, -1), new Vector2(0, -2)),
            Color.BLACK, Color.BLACK
        );
        SproutsEdge v6v5 = v5v6.reversed();

        SproutsFaces faces = new SproutsFaces(
            v1v4, v1v9, 
            v2v4, v2v6, v2v7,
            v4v1, v4v2, v4v5,
            v5v4, v5v6,
            v6v2, v6v5,
            v7v2,
            v9v1
        );

        Set<Set<SproutsEdge>> expected = new HashSet<>();
        Set<SproutsEdge> f1 =
            new HashSet<>(Arrays.asList(v4v2, v2v6, v6v5, v5v4));
        Set<SproutsEdge> f2 =
            new HashSet<>(Arrays.asList(
                v4v1, v1v9, v9v1, v1v4, v4v5, v5v6, v6v2, v2v7, v7v2, v2v4
            ));
        expected.add(f1);
        expected.add(f2);
        assertEquals(expected, faces.faces());
    }
    @Test
    public void detectsACobwebEdgeThatIsInTwoFaces() {
        SproutsEdge v1v2 = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(1, 1), new Vector2(3, 0)),
            Color.BLACK, Color.BLACK);
        SproutsEdge v2v1 = v1v2.reversed();
        SproutsEdge v1v2dot = new SproutsEdge(
            new Polyline.WrappedList(Vector2.Zero, new Vector2(1, -1), new Vector2(3, 0)),
            Color.BLACK, Color.BLACK);
        SproutsEdge v2v1dot = v1v2dot.reversed();

        assertThat(
            new SproutsFaces(
                v1v2, v2v1, v1v2dot, v2v1dot
            ).findFirstCobwebEdgeInTwoFaces(Color.RED),
            is(not(nullValue()))
        );
    }
}
