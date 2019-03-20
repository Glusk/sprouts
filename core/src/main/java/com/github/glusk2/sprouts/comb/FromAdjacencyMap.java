package com.github.glusk2.sprouts.comb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.geom.Polyline;

/** Graph from adjacency map. */
public final class FromAdjacencyMap implements PlanarGraph {
    /** Adjacency map representation of {@code this} graph. */
    private final Map<Vertex, TreeSet<Vertex>> adjacencyMap;

    /**
     * Constructs a new planar graph from an adjacency map representation.
     *
     * @param adjacencyMap adjacency map graph representation
     */
    public FromAdjacencyMap(final Map<Vertex, TreeSet<Vertex>> adjacencyMap) {
        this.adjacencyMap = adjacencyMap;
    }

    @Override
    public Set<Edge> edges() {
        Set<Edge> edges = new HashSet<Edge>();
        for (Vertex from : adjacencyMap.keySet()) {
            for (Vertex to: adjacencyMap.get(from)) {
                edges.add(new Edge(from, to));
            }
        }
        return edges;
    }

    @Override
    public Set<Vertex> vertices() {
        return adjacencyMap.keySet();
    }

    @Override
    public List<Face> faces() {
        List<Face> faces = new ArrayList<Face>();

        Set<Edge> nextFace = new HashSet<Edge>();
        Set<Edge> burntEdges = new HashSet<Edge>();
        for (Edge edge : edges()) {
            Edge nextEdge = edge;
            while (
                !nextFace.contains(nextEdge)
             && !burntEdges.contains(nextEdge)
            ) {
                burntEdges.add(nextEdge);
                nextFace.add(nextEdge);
                nextEdge = nextEdge(nextEdge.reversed());
            }
            if (!nextFace.isEmpty()) {
                faces.add(new PlanarFace(nextFace));
                nextFace = new HashSet<Edge>();
            }
        }
        return faces;
    }

    /**
     * Returns the first edge that starts in {@code current.from()} and comes
     * strictly <em>after</em> {@code current} in the clock-wise order of
     * edges around vertex {@code current.from()}.
     * <p>
     * More formally this method returns:
     * <pre>
     * new Edge(
     *     current.from(),
     *     // ... the first vertex in clock-wise order of vertices after
     *     // "current.to()" (with respect to "current.from()")
     * )
     * </pre>
     *
     * @param current the edge that directly precedes the return value in
     *                clock-wise order
     * @return the next edge in clock-wise order
     */
    private Edge nextEdge(final Edge current) {
        TreeSet<Vertex> neighborsSet = adjacencyMap.get(current.from());
        List<Vertex> neighborsList = new ArrayList<Vertex>(neighborsSet);
        if (!edges().contains(current)) {
            neighborsList.add(current.to());
            Collections.sort(neighborsList, neighborsSet.comparator());
        }
        return
            new Edge(
                current.from(),
                new CircularIterator<Vertex>(
                    neighborsList,
                    Collections.binarySearch(
                        neighborsList,
                        current.to(),
                        neighborsSet.comparator()
                    )
                ).next()
            );
    }

    @Override
    public Face nextMoveFace(final Polyline nextMove) {
        List<Vector2> nextMovePoints = nextMove.points();
        List<Vertex> vertices = new ArrayList<Vertex>(vertices());
        for (int i = 0; i < vertices.size(); i++) {
            Vertex from = vertices.get(i);
            if (from.position().equals(nextMovePoints.get(0))) {
                Edge firstSegment =
                    new Edge(
                        from,
                        new Vertex(nextMovePoints.get(1), -1)
                    );
                Edge faceEdge = nextEdge(firstSegment);
                for (Face f : faces()) {
                    if (f.boundary().contains(faceEdge)) {
                        return f;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Illegal move!");
    }
}
