package com.github.glusk2.sprouts.comb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

/** Test class for {@code ClockwiseComparator}. */
public final class ClockwiseComparatorTest {
    /** Checks the basic ordering of vertices. */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void sortsVerticesInRadialOrder() {
        Comparator<Vertex> comparator =
            new ClockwiseComparator(new Vector2(0, 0));
        List<Vertex> vertices =
            Arrays.asList(
                new Vertex(new Vector2(2, 2), 0),
                new Vertex(new Vector2(0, 3), 1),
                new Vertex(new Vector2(-4, 1), 2),
                new Vertex(new Vector2(-1, 2), 3),
                new Vertex(new Vector2(3, -1), 4),
                new Vertex(new Vector2(-1, -2), 5)
            );
        Collections.sort(vertices, comparator);

        List<Integer> order = Arrays.asList(5, 2, 3, 1, 0, 4);
        int index = 0;
        while (order.get(index) != vertices.get(0).label()) {
            index++;
        }
        Iterator<Vertex> vertexIterator =
            new CircularIterator<Vertex>(vertices, 0);
        Iterator<Integer> orderIterator =
            new CircularIterator<Integer>(order, index);
        for (int i = 0; i < order.size(); i++) {
            int expectedLabel = orderIterator.next();
            int actualLabel = vertexIterator.next().label();
            assertEquals(
                String.format(
                    "Sorting failed, labels don't match:"
                  + "%n expected: %d, actual: %d",
                    expectedLabel,
                    actualLabel
                ),
                expectedLabel,
                actualLabel
            );
        }
    }

    /** Checks that opposite clock pointers are not equal. */
    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void addsOppositeVerticesToTreeSet() {
        Comparator<Vertex> comparator =
            new ClockwiseComparator(new Vector2(0, 0));
        TreeSet<Vertex> vertices = new TreeSet<Vertex>(comparator);
        vertices.add(new Vertex(new Vector2(1, 0), 0));
        vertices.add(new Vertex(new Vector2(-1, 0), 1));

        assertTrue(vertices.size() == 2);
    }
}
