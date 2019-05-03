package com.github.glusk2.sprouts.comb;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;

import org.junit.Test;


/** PresetGraph test class. */
public final class PresetGraphTest {
    /** Checks that the graph implementation can build correct faces. */
    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void buildsCorrectFaces() {
        Vertex v1 = new PresetVertex(new Vector2(4, 0), 1);
        Vertex v2 = new PresetVertex(new Vector2(0, 0), 2);

        Vertex v4 = new PresetVertex(new Vector2(2, 0), 4);
        Vertex v5 = new PresetVertex(new Vector2(1, -1), 5);
        Vertex v6 = new PresetVertex(new Vector2(0, -2), 6);
        Vertex v7 = new PresetVertex(new Vector2(0, 2), 7);

        Vertex v9 = new PresetVertex(new Vector2(6, 0), 9);

        Map<Vertex, LocalRotations> rotationsList =
            new HashMap<Vertex, LocalRotations>();

        rotationsList.put(
            v1,
            new PresetRotations(v1).with(
                new StraightLineEdge(v4),
                new StraightLineEdge(v9)
            )
        );
        rotationsList.put(
            v2,
            new PresetRotations(v2).with(
                new StraightLineEdge(v4),
                new StraightLineEdge(v6),
                new StraightLineEdge(v7)
            )
        );
        rotationsList.put(
            v4,
            new PresetRotations(v4).with(
                new StraightLineEdge(v1),
                new StraightLineEdge(v5),
                new StraightLineEdge(v2)
            )
        );
        rotationsList.put(
            v5,
            new PresetRotations(v5).with(
                new StraightLineEdge(v4),
                new StraightLineEdge(v6)
            )
        );
        rotationsList.put(
            v6,
            new PresetRotations(v6).with(
                new StraightLineEdge(v5),
                new StraightLineEdge(v2)
            )
        );
        rotationsList.put(
            v7,
            new PresetRotations(v7).with(
                new StraightLineEdge(v2)
            )
        );
        rotationsList.put(
            v9,
            new PresetRotations(v9).with(
                new StraightLineEdge(v1)
            )
        );

        List<Set<CompoundEdge>> correctFaces =
            new ArrayList<Set<CompoundEdge>>();
        correctFaces.add(
            new HashSet<CompoundEdge>(
                Arrays.asList(
                    new CompoundEdge.Wrapped(new StraightLineEdge(v4, v2)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v2, v6)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v6, v5)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v5, v4))
                )
            )
        );
        correctFaces.add(
            new HashSet<CompoundEdge>(
                Arrays.asList(
                    new CompoundEdge.Wrapped(new StraightLineEdge(v4, v1)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v1, v9)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v9, v1)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v1, v4)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v4, v5)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v5, v6)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v6, v2)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v2, v7)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v7, v2)),
                    new CompoundEdge.Wrapped(new StraightLineEdge(v2, v4))
                )
            )
        );
        assertTrue(
            "Faces don't match!",
            correctFaces.containsAll(
                new PresetGraph(rotationsList).faces()
            )
        );
    }
}
