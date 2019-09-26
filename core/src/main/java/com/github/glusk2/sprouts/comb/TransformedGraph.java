package com.github.glusk2.sprouts.comb;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * This object is a cache for the result of
 * {@link GraphTransformation#transformed()}.
 */
public final class TransformedGraph implements Graph {
    /** The GraphTransformation object that produces {@code this} Graph. */
    private final GraphTransformation transformation;
    /** The cached value of {@code transformation.transformed()}. */
    private Graph cachedTransformation;

    /**
     * Constructs a new TransformedGraph that is produced via
     * {@code transformation.transformed()}.
     *
     * @param transformation the GraphTransformation object that produces
     *                       {@code this} Graph
     */
    public TransformedGraph(final GraphTransformation transformation) {
        this.transformation = transformation;
    }

    @Override
    public void renderTo(final ShapeRenderer renderer) {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        cachedTransformation.renderTo(renderer);
    }

    @Override
    public List<Set<CompoundEdge>> faces() {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.faces();
    }

    @Override
    public Set<CompoundEdge> edges() {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.edges();
    }

    @Override
    public Set<Vertex> vertices() {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.vertices();
    }

    @Override
    public Graph with(final Vertex additionalVertex) {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.with(additionalVertex);
    }

    @Override
    public Graph with(final Vertex origin, final DirectedEdge direction) {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.with(origin, direction);
    }

    @Override
    public Graph without(final Vertex origin, final DirectedEdge direction) {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.without(origin, direction);
    }

    @Override
    public Set<CompoundEdge> edgeFace(final CompoundEdge edge) {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.edgeFace(edge);
    }

    @Override
    public Graph simplified() {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.simplified();
    }

    @Override
    public boolean isAliveSprout(final Vertex vertex) {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.isAliveSprout(vertex);
    }

    @Override
    public int vertexDegree(final Vertex vertex, final Color edgeColor) {
        if (cachedTransformation == null) {
            cachedTransformation = transformation.transformed();
        }
        return cachedTransformation.vertexDegree(vertex, edgeColor);
    }
}
