package com.github.glusk2.sprouts.comb;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import org.junit.BeforeClass;
import org.junit.Test;

/** IntersectionSegmentFace test class. */
public final class IntersectionSegmentFaceTest {
    /** A simple face, shared by various tests. */
    private static Set<CompoundEdge> face;

    /** Sets up a simple square box face, used for simple tests. */
    @BeforeClass
    public static void setupFace() {
        face = new HashSet<CompoundEdge>();
        Vertex v0 = new PresetVertex(Vector2.Zero, "00");
        Vertex v1 = new PresetVertex(new Vector2(2, 0), "20");
        Vertex v2 = new PresetVertex(new Vector2(2, 2), "22");
        Vertex v3 = new PresetVertex(new Vector2(0, 2), "02");
        face.add(
            new CompoundEdge.Wrapped(
                new StraightLineEdge(v0, v1)
            )
        );
        face.add(
            new CompoundEdge.Wrapped(
                new StraightLineEdge(v1, v2)
            )
        );
        face.add(
            new CompoundEdge.Wrapped(
                new StraightLineEdge(v2, v3)
            )
        );
        face.add(
            new CompoundEdge.Wrapped(
                new StraightLineEdge(v3, v0)
            )
        );
    }

    /** Checks the basic functionality - finding a simple intersection. */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void detectsSimpleIntersection() {
        assertEquals(
            new IntersectionSegmentFace(
                face,
                new Vector2(1, 1),
                new Vector2(3, 1)
            ).result(),
            new PresetVertex(
                Color.RED,
                new Vector2(2, 1),
                "21"
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
            new IntersectionSegmentFace(
                face,
                new Vector2(3, 1),
                new Vector2(4, 1)
            ).result(),
            new VoidVertex(null)
        );
    }
}
