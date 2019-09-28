package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * A NearestSproutSearch object can find the nearest sprout in Graph to a
 * specified Vector2 position.
 */
public final class NearestSproutSearch implements VertexSearch {
    /** The default sprout color. */
    private static final Color DEFAULT_SPROUT_COLOR = Color.BLACK;
    /** The Graph with its set of Vertices. */
    private final Graph graph;
    /**
     * This VertexSearch finds and returns the sprout in {@code graph} that's
     * nearest to {@code position}.
     */
    private final Vector2 position;
    /** The color of the sprout Vertices in {@code graph}. */
    private Color sproutColor;

    /**
     * Constructs a new NearestSproutSearch object by specifying the
     * {@code graph} and {@code position}, using the default sprout color.
     *
     * @param graph the Graph with its set of Vertices
     * @param position this VertexSearch finds and returns the sprout in
     *                 {@code graph} that's nearest to {@code position}
     */
    public NearestSproutSearch(
        final Graph graph,
        final Vector2 position
    ) {
        this(graph, position, DEFAULT_SPROUT_COLOR);
    }

    /**
     * Constructs a new NearestSproutSearch object by specifying the
     * {@code graph} and {@code position} and {@code sproutColor}.
     *
     * @param graph the Graph with its set of Vertices
     * @param position this VertexSearch finds and returns the sprout in
     *                 {@code graph} that's nearest to {@code position}
     * @param sproutColor the color of the sprout Vertices in {@code graph}
     */
    public NearestSproutSearch(
        final Graph graph,
        final Vector2 position,
        final Color sproutColor
    ) {
        this.graph = graph;
        this.position = position.cpy();
        this.sproutColor = sproutColor;
    }
    /**
     * Finds and returns the sprout nearest to {@code position} in
     * {@code graph}.
     * <p>
     * If {@code graph} has no sprouts, a new instance of {@code VoidVertex}
     * is returned.
     * <p>
     * If the result is more than one sprout (more than one sprout is at the
     * same, minimal distance to {@code position}), the first encountered is
     * returned.
     *
     * @return the sprout closest to {@code position} in {@code graph}
     */
    @Override
    public Vertex result() {
        float minDistance = Float.MAX_VALUE;
        Vertex minVertex = new VoidVertex(null);
        for (Vertex v : graph.vertices()) {
            if (v.color().equals(sproutColor)) {
                float nextDistance = v.position().dst(position);
                if (nextDistance < minDistance) {
                    minDistance = nextDistance;
                    minVertex = v;
                }
            }
        }
        return minVertex;
    }
}
