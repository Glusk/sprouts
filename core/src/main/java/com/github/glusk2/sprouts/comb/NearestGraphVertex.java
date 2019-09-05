package com.github.glusk2.sprouts.comb;

import com.badlogic.gdx.math.Vector2;

/**
 * A NearestGraphVertex object can find the nearest Vertex in Graph to a
 * specified Vector2 position.
 */
public final class NearestGraphVertex implements VertexSearch {
    /** The Graph with its set of Vertices. */
    private final Graph graph;
    /**
     * This VertexSearch finds and returns the Vertex in {@code graph} that's
     * nearest to {@code position}.
     */
    private final Vector2 position;

    /**
     * Constructs a new NearestGraphVertex object by specifying the
     * {@code graph} and {@code position}.
     *
     * @param graph the Graph with its set of Vertices
     * @param position this VertexSearch finds and returns the Vertex in
     *                 {@code graph} that's nearest to {@code position}
     */
    public NearestGraphVertex(
        final Graph graph,
        final Vector2 position
    ) {
        this.graph = graph;
        this.position = position.cpy();
    }

    /**
     * Finds and returns the Vertex nearest to {@code position} in
     * {@code graph.vertices()}.
     * <p>
     * If {@code graph.vertices()} is an empty set, a new instance of
     * {@code VoidVertex} is returned.
     * <p>
     * If the result is more than one Vertex (more than one Vertex is at the
     * same, minimal distance to {@code position}), the first encountered is
     * returned.
     *
     * @return the Vertex closest to {@code position} in
     *         {@code graph.vertices()}
     */
    @Override
    public Vertex result() {
        float minDistance = Float.MAX_VALUE;
        Vertex minVertex = new VoidVertex(null);
        for (Vertex v : graph.vertices()) {
            float nextDistance = v.position().dst(position);
            if (nextDistance < minDistance) {
                minDistance = nextDistance;
                minVertex = v;
            }
        }
        return minVertex;
    }
}
