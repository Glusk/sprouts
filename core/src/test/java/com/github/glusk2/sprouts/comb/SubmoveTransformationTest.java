package com.github.glusk2.sprouts.comb;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

/** The SubmoveTransformation test class. */
public final class SubmoveTransformationTest {
    /**
     * This test checks that a Submove, which crosses the cobweb, splits the
     * cobweb and returns the simplified Graph.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void splitsTheCobwebOnIntersection() {
        Vertex s0 = new PresetVertex(new Vector2(2, 0), "s0");
        Vertex s1 = new PresetVertex(new Vector2(0, 0), "s1");
        Vertex s2 = new PresetVertex(new Vector2(0, 2), "s2");

        Vertex c0 =
            new PresetVertex(
                Color.RED,
                new Vector2(0, 1),
                "c0"
            );

        Map<Vertex, LocalRotations> rotationsList =
            new HashMap<Vertex, LocalRotations>();
        rotationsList.put(
            s0,
            new PresetRotations(s0).with(
                new StraightLineEdge(s1)
            )
        );
        rotationsList.put(
            s1,
            new PresetRotations(s1).with(
                new StraightLineEdge(s0),
                new StraightLineEdge(s2)
            )
        );
        rotationsList.put(
            s2,
            new PresetRotations(s2).with(
                new StraightLineEdge(s1)
            )
        );
        Graph currentState = new PresetGraph(rotationsList);

        CompoundEdge submove =
            new CompoundEdge.Wrapped(
                s1,
                new PolylineEdge(
                    s1.color(),
                    c0.color(),
                    Arrays.asList(
                        new Vector2(.5f, .25f),
                        new Vector2(.5f, .75f),
                        c0.position()
                    )
                )
            );

        CompoundEdge cobwebEdge0 =
            new CompoundEdge.Wrapped(
                new StraightLineEdge(s0, s1)
            );
        CompoundEdge cobwebEdge1 =
            new CompoundEdge.Wrapped(
                new StraightLineEdge(c0, s2)
            );
        assertEquals(
              "Combinatorial state was not updated properly: "
            + "edge sets don't match!",
            new HashSet<CompoundEdge>(
                Arrays.asList(
                    cobwebEdge0,
                    new ReversedCompoundEdge(cobwebEdge0),
                    cobwebEdge1,
                    new ReversedCompoundEdge(cobwebEdge1),
                    submove,
                    new ReversedCompoundEdge(submove)
                )
            ),
            new SubmoveTransformation(
                submove,
                currentState
            )
            .transformed()
            .edges()
        );
    }
}
