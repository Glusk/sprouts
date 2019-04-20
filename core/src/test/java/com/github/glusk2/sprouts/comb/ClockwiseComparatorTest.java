package com.github.glusk2.sprouts.comb;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

/** Test class for {@code ClockwiseComparator}. */
public final class ClockwiseComparatorTest {
    /** Checks that opposite clock pointers are not equal. */
    @Test
    public void addsOppositeVerticesToTreeSet() {
        Comparator<DirectedEdge> comparator =
            new ClockwiseComparator(
                new PresetVertex(
                    new Vector2(0, 0),
                    "v"
                )
            );
        TreeSet<DirectedEdge> edges = new TreeSet<DirectedEdge>(comparator);
        edges.add(
            new StraightLineEdge(
                new PresetVertex(
                    new Vector2(1, 0),
                    "a"
                )
            )
        );
        edges.add(
            new StraightLineEdge(
                new PresetVertex(
                    new Vector2(-1, 0),
                    "c"
                )
            )
        );
        assertTrue(edges.size() == 2);
    }
}
