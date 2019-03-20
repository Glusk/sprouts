package com.github.glusk2.sprouts.comb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.geom.Polyline;

import org.junit.Test;

/** Test class for {@code FromAdjacencyMap}. */
public final class FromAdjacencyMapTest {
    /** Checks that the graph implementation can build faces. */
    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void buildsCorrectFaces() {
        Vertex v1 = new Vertex(new Vector2(4, 0), 1);
        Vertex v2 = new Vertex(new Vector2(0, 0), 2);

        Vertex v4 = new Vertex(new Vector2(2, 0), 4);
        Vertex v5 = new Vertex(new Vector2(1, -1), 5);
        Vertex v6 = new Vertex(new Vector2(0, -2), 6);
        Vertex v7 = new Vertex(new Vector2(0, 2), 7);

        Vertex v9 = new Vertex(new Vector2(6, 0), 9);

        Map<Vertex, TreeSet<Vertex>> adjacencyMap =
            new HashMap<Vertex, TreeSet<Vertex>>();

        TreeSet<Vertex> nextSet = null;

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v1));
        nextSet.add(v4);
        nextSet.add(v9);
        adjacencyMap.put(v1, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v2));
        nextSet.add(v4);
        nextSet.add(v6);
        nextSet.add(v7);
        adjacencyMap.put(v2, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v4));
        nextSet.add(v1);
        nextSet.add(v5);
        nextSet.add(v2);
        adjacencyMap.put(v4, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v5));
        nextSet.add(v4);
        nextSet.add(v6);
        adjacencyMap.put(v5, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v6));
        nextSet.add(v5);
        nextSet.add(v2);
        adjacencyMap.put(v6, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v7));
        nextSet.add(v2);
        adjacencyMap.put(v7, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v9));
        nextSet.add(v1);
        adjacencyMap.put(v9, nextSet);

        List<Face> correctFaces =
            Arrays.asList(
                (Face) new PlanarFace(
                    new HashSet<Edge>(
                        Arrays.asList(
                            new Edge(v4, v2),
                            new Edge(v2, v6),
                            new Edge(v6, v5),
                            new Edge(v5, v4)
                        )
                    )
                ),
                (Face) new PlanarFace(
                    new HashSet<Edge>(
                        Arrays.asList(
                            new Edge(v4, v1),
                            new Edge(v1, v9),
                            new Edge(v9, v1),
                            new Edge(v1, v4),
                            new Edge(v4, v5),
                            new Edge(v5, v6),
                            new Edge(v6, v2),
                            new Edge(v2, v7),
                            new Edge(v7, v2),
                            new Edge(v2, v4)
                        )
                    )
                )
            );
        assertTrue(
            "Faces don't match!",
            correctFaces.containsAll(
                new FromAdjacencyMap(adjacencyMap).faces()
            )
        );
    }

    /**
     * This test checks that an unfinished move is placed into the correct
     * face.
     * <p>
     * A move consists of one or more polyline segments. The first segment
     * from the origin point determines in which face the next move is
     * being drawn.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void placesUnfinishedMoveIntoTheCorrectFace() {
        Vertex v1 = new Vertex(new Vector2(-4, 0), 1);
        Vertex v4 = new Vertex(new Vector2(4, 0), 4);
        Vertex v5 = new Vertex(new Vector2(0, 4), 5);
        Vertex v7 = new Vertex(new Vector2(0, -4), 7);

        Map<Vertex, TreeSet<Vertex>> adjacencyMap =
            new HashMap<Vertex, TreeSet<Vertex>>();

        TreeSet<Vertex> nextSet = null;

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v1));
        nextSet.add(v5);
        nextSet.add(v7);
        adjacencyMap.put(v1, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v4));
        nextSet.add(v5);
        nextSet.add(v7);
        adjacencyMap.put(v4, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v5));
        nextSet.add(v1);
        nextSet.add(v4);
        adjacencyMap.put(v5, nextSet);

        nextSet = new TreeSet<Vertex>(new ClockwiseComparator(v7));
        nextSet.add(v1);
        nextSet.add(v4);
        adjacencyMap.put(v7, nextSet);

        Polyline unfinishedMove =
            new Polyline.WrappedList(
                Arrays.asList(
                    v7.position(),
                    new Vector2(0, -2),
                    new Vector2(-2, 0),
                    new Vector2(0, 2)
                )
        );

        Face correctFace = new PlanarFace(
            new HashSet<Edge>(
                Arrays.asList(
                    new Edge(v4, v5),
                    new Edge(v5, v1),
                    new Edge(v1, v7),
                    new Edge(v7, v4)
                )
            )
        );

        assertEquals(
            correctFace,
            new FromAdjacencyMap(adjacencyMap).nextMoveFace(unfinishedMove)
        );
    }
}
