package com.github.glusk2.sprouts.core.comb;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

import org.junit.BeforeClass;
import org.junit.Test;

/** FaceIntersectionSearch test class. */
public final class FaceIntersectionSearchTest {
    /** A simple face, shared by various tests. */
    private static Set<SproutsEdge> face;

    /** Sets up a simple square box face, used for simple tests. */
    @BeforeClass
    public static void setupFace() {
        face = new HashSet<SproutsEdge>(
            Arrays.asList(
                new SproutsEdge(
                    new Polyline.WrappedList(
                        Vector2.Zero,
                        new Vector2(2, 0)
                    ),
                    Color.BLACK, Color.BLACK
                ),
                new SproutsEdge(
                    new Polyline.WrappedList(
                        new Vector2(2, 0),
                        new Vector2(2, 2)
                    ),
                    Color.BLACK, Color.BLACK
                ),
                new SproutsEdge(
                    new Polyline.WrappedList(
                        new Vector2(2, 2),
                        new Vector2(0, 2)
                    ),
                    Color.BLACK, Color.BLACK
                ),
                new SproutsEdge(
                    new Polyline.WrappedList(
                        new Vector2(0, 2),
                        Vector2.Zero
                    ),
                    Color.BLACK, Color.BLACK
                )
            )
        );
    }

    /** Checks the basic functionality - finding a simple intersection. */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void detectsSimpleIntersection() {
        assertEquals(
            new FaceIntersectionSearch(
                face,
                new Vector2(1, 1),
                new Vector2(3, 1)
            ).result(),
            new PresetVertex(
                Color.RED,
                new Vector2(2, 1)
            )
        );
    }

    /**
     * Checks that there is no intersection detected between a face and the
     * segment touching the face.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void detectsThatThereIsNoIntersection() {
        assertEquals(
            new FaceIntersectionSearch(
                face,
                new Vector2(3, 1),
                new Vector2(4, 1)
            ).result(),
            new VoidVertex()
        );
    }
}
