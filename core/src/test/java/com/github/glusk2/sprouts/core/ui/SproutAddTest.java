package com.github.glusk2.sprouts.core.ui;

import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.comb.CompoundEdge;
import com.github.glusk2.sprouts.core.comb.Graph;
import com.github.glusk2.sprouts.core.comb.LocalRotations;
import com.github.glusk2.sprouts.core.comb.PresetGraph;
import com.github.glusk2.sprouts.core.comb.PresetRotations;
import com.github.glusk2.sprouts.core.comb.PresetVertex;
import com.github.glusk2.sprouts.core.comb.StraightLineEdge;
import com.github.glusk2.sprouts.core.comb.Vertex;

import org.junit.Test;

/** A test class for SproutAdd. */
public class SproutAddTest {
    /**
     * Checks if the middle sprout placed on the intersection between the new
     * move and existing cobweb edge is added to the combinatorial
     * representation correctly.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void correctlyPlacesNewSproutOnCobwebMoveIntersection() {
        Vertex v1 = new PresetVertex(new Vector2(-4, 0));
        Vertex v2 = new PresetVertex(new Vector2(4, 0));

        Map<Vertex, LocalRotations> rotationsList =
            new HashMap<Vertex, LocalRotations>();
        rotationsList.put(
            v1,
            new PresetRotations(v1).with(
                new StraightLineEdge(v2)
            )
        );
        rotationsList.put(
            v2,
            new PresetRotations(v2).with(
                new StraightLineEdge(v1)
            )
        );
        Graph currentState = new PresetGraph(rotationsList, 1, 16);

        Graph nextState = new SproutAdd(
            currentState,
            1,
            16,
            v1,
            Arrays.asList(
                new Vector2(-3, 2),
                new Vector2(0, 2),
                new Vector2(2, 0),
                new Vector2(0, -2),
                new Vector2(-3, -2),
                v1.position()
            ),
            new Rectangle(-20, -20, 200, 200)
        ).touchUp(new Vector2(2, 0)).currentState();

        assertFalse(
            "Cobweb not torn!",
            nextState.edges().contains(
                new CompoundEdge.Wrapped(v1, new StraightLineEdge(v2))
            )
        );
    }
}
