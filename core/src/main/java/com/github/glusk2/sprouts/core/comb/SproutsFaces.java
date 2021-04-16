package com.github.glusk2.sprouts.core.comb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.Color;

public final class SproutsFaces {
    private final Set<SproutsEdge> edges;

    private Map<Vertex, SproutsRotations> rotationsCache;
    private Set<Set<SproutsEdge>> faceCache;

    public SproutsFaces(SproutsEdge... edges) {
        this(new HashSet<>(Arrays.asList(edges)));
    }
    public SproutsFaces(Set<SproutsEdge> edges) {
        this.edges = edges;
    }
    private Map<Vertex, SproutsRotations> makeRotations() {
        Map<Vertex, SortedSet<SproutsEdge>> rotations = new HashMap<>();
        for (SproutsEdge edge : edges) {
            SortedSet<SproutsEdge> tmp =
                rotations.getOrDefault(
                    edge.from(),
                    new TreeSet<SproutsEdge>()
                );
            tmp.add(edge);
            rotations.put(
                edge.from(),
                tmp
            );
        }
        Map<Vertex, SproutsRotations> result = new HashMap<>();
        for (Vertex key : rotations.keySet()) {
            result.put(key, new SproutsRotations(rotations.get(key)));
        }
        return result;
    }
    public Set<Set<SproutsEdge>> faces() {
        if (faceCache != null) {
            return faceCache;
        }
        if (rotationsCache == null) {
            rotationsCache = this.makeRotations();
        }
        Map<Vertex, SproutsRotations> rotations = rotationsCache;
        Set<Set<SproutsEdge>> faces = new HashSet<Set<SproutsEdge>>();
        
        Set<SproutsEdge> nextFace = new HashSet<SproutsEdge>();
        Set<SproutsEdge> burntEdges = new HashSet<SproutsEdge>();
        for (SproutsEdge edge : edges) {
            SproutsEdge nextEdge = edge;
            while (
                !nextFace.contains(nextEdge)
             && !burntEdges.contains(nextEdge)
            ) {
                burntEdges.add(nextEdge);
                nextFace.add(nextEdge);
                SproutsEdge rev = nextEdge.reversed();
                nextEdge = rotations.get(rev.from()).next(rev);
            }
            if (!nextFace.isEmpty()) {
                faces.add(nextFace);
                nextFace = new HashSet<SproutsEdge>();
            }
        }
        faceCache = faces;
        return faces;
    }

    /**
     * Returns the face in which the {@code submove} is drawn.
     * <p>
     * A submove is drawn into a face that <strong>contains</strong> the
     * first directed edge <em>after</em> the {@code submove} - {@code next}.
     * <p>
     * {@code next} is defined as:
     * <pre>
     * // Map&lt;Vertex, SproutsRotations&gt; rotations = ...
     * SproutsEdge next = rotations.get(submove.from()).next(submove);
     * </pre>
     *
     * @param submove A submove in the game of sprouts. The submove need not be
     *                completed.
     * @return the face in whish this {@code submove} is drawn as a set of
     *         directed edges
     * @throws IllegalArgumentException if {@code submove} is not connected to
     * the graph whose faces are represented by {@code this} object
     */
    public Set<SproutsEdge> drawnIn(final SproutsEdge submove) {
        if (rotationsCache == null) {
            rotationsCache = this.makeRotations();
        }
        Map<Vertex, SproutsRotations> rotations = rotationsCache;
        Set<Set<SproutsEdge>> faces = this.faces();

        SproutsEdge next = rotations.get(submove.from()).next(submove);
        for (Set<SproutsEdge> face : faces) {
            if (face.contains(next)) {
                return face;
            }
        }
        throw new IllegalArgumentException(
            "The submove is not connected to the graph whose faces are "
          + "represented by \"this\" object."
        );
    }
    /**
     * Finds a pair of cobweb edges that reside in two separate
     * faces and returns either one of the edges in a pair as a result.
     * <p>
     * If there are multiple such pairs, only one is detected. Which one
     * exactly is undefined.
     * <p>
     * If such a pair does not exist, this method returns {@code null}
     *
     * @param cobwebColor the colour of the cobweb edges
     * @return returns one edge from a pair of opposite cobweb edges that
     *         reside in two separate faces
     */
    public SproutsEdge findFirstCobwebEdgeInTwoFaces(Color cobwebColor) {
        Set<Set<SproutsEdge>> faces = this.faces();
        Set<SproutsEdge> burntRed = new HashSet<>();
        for (SproutsEdge e : this.edges) {
            if (e.color().equals(cobwebColor) && !burntRed.contains(e)) {
                burntRed.add(e);
                burntRed.add(e.reversed());
                for (Set<SproutsEdge> face : faces) {
                    if (face.contains(e) && !face.contains(e.reversed())) {
                        // e and e.reversed() are in different faces!
                        return e;
                    }
                }
            }
        }
        return null;
    }
}
