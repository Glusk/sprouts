package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents the degree of a {@code vertex} in a {@code graph}.
 * <p>
 * Only the edges of the specified {@code color} are counted.
 */
public final class VertexDegree extends Number {

    private Vertex vertex;
    private SproutsGameState graph;
    private Color edgeColor;

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
