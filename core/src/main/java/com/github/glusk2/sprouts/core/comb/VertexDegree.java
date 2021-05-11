package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents the degree of a {@code vertex} in a {@code graph}.
 * <p>
 * Only the edges of the specified {@code color} are counted.
 */
public final class VertexDegree extends Number {
    /** The vertex whose degree we are interested in. */
    private Vertex vertex;
    /** The graph in which there is the {@code vertex}. */
    private SproutsGameState graph;
    /**
     * Only look for edges of this color when calculating the degree of
     * {@code vertex}.
      */
    private Color edgeColor;

    /**
     * Creates a new Vertex degree number.
     *
     * @param vertex the vertex whose degree we are interested in
     * @param graph the graph in which there is the {@code vertex}
     * @param edgeColor only look for edges of this color when
     * calculating the degree of {@code vertex}
     */
    public VertexDegree(
        final Vertex vertex,
        final SproutsGameState graph,
        final Color edgeColor
    ) {
        this.vertex = vertex;
        this.graph = graph;
        this.edgeColor = edgeColor;
    }

    @Override
    public double doubleValue() {
        return this.intValue();
    }
    @Override
    public float floatValue() {
        return this.intValue();
    }
    @Override
    public int intValue() {
        int degree = 0;
        for (SproutsEdge edge : graph.edges()) {
            if (
                edge.from().equals(vertex)
             && edge.color().equals(edgeColor)
            ) {
                degree++;
            }
        }
        return degree;
    }
    @Override
    public long longValue() {
        return this.intValue();
    }
}
