package com.github.glusk2.sprouts.core.comb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public final class SproutsFaces {
    private Set<SproutsEdge> edges;
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
        Map<Vertex, SproutsRotations> rotations = this.makeRotations();
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
        return faces;
    }
}
