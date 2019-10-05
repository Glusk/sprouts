package com.github.glusk2.sprouts.core.comb;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Creates a new Graph by connecting {@code numOfSprouts} with a cobweb in
 * a circle around the center of the {@code gameBounds}.
 */
public final class GraphCreation implements GraphTransformation {
    /**
     * Scales the minimal {@code gameBound} dimension to pad the circle of
     * sprouts.
     */
    private static final float DIMENSION_SCALE = .6f;
    /**Full circle, in degrees. */
    private static final int FULL_CIRCLE = 360;

    /** The number of sprouts to generate. */
    private final int numOfSprouts;
    /** The Graph to add {@code numOfSprouts} to. */
    private final Graph existingGraph;
    /** The game bounds rectangle. */
    private final Rectangle gameBounds;

    /**
     * Constructor that builds an empty graph for {@code this} GraphCreation.
     * <p>
     * Equivalent to:
     * <pre>
     * new GraphCreation(
     *     numOfSprouts,
     *         new PresetGraph(
     *             new HashMap&lt;Vertex, LocalRotations&gt;(),
     *             lineThickness,
     *             circleSegmentCount
     *         ),
     *     gameBounds
     * );
     * </pre>
     *
     * @param numOfSprouts the number of sprouts to generate
     * @param lineThickness line thickness of edges; required to build the
     *                      empty Graph
     * @param circleSegmentCount circle segment count; required to build the
     *                      empty Graph
     * @param gameBounds the game bounds rectangle
     */
    public GraphCreation(
        final int numOfSprouts,
        final float lineThickness,
        final int circleSegmentCount,
        final Rectangle gameBounds
    ) {
        this(
            numOfSprouts,
            new PresetGraph(
                new HashMap<Vertex, LocalRotations>(),
                lineThickness,
                circleSegmentCount
            ),
            gameBounds
        );
    }

    /**
     * Adds {@code numOfSprouts} to {@code existingGraph}. The sprouts are
     * guaranteed to be generated within the {@code gameBounds}.
     *
     * @param numOfSprouts the number of sprouts to generate
     * @param existingGraph the Graph to add {@code numOfSprouts} to
     * @param gameBounds the game bounds rectangle
     */
    public GraphCreation(
        final int numOfSprouts,
        final Graph existingGraph,
        final Rectangle gameBounds
    ) {
        this.numOfSprouts = numOfSprouts;
        this.existingGraph = existingGraph;
        this.gameBounds = gameBounds;
    }

    @Override
    public Graph transformed() {
        Vector2 center = gameBounds.getCenter(new Vector2());
        float minDimension =
            Math.min(
                gameBounds.getWidth(),
                gameBounds.getHeight()
            );
        float radius = (DIMENSION_SCALE * minDimension) / 2;
        Vector2 v0 = center.cpy().add(radius, 0);
        Vector2 clockPointer = v0.cpy().sub(center);

        Vertex[] vertices = new Vertex[numOfSprouts];
        for (int i = 0; i < numOfSprouts; i++) {
            vertices[i] =
                new PresetVertex(
                    center.cpy().add(
                        clockPointer.cpy().rotate(
                            1f * i / numOfSprouts * FULL_CIRCLE
                        )
                    ),
                    (String) null
                );
        }

        Graph result = existingGraph;
        for (int i = 0; i < numOfSprouts - 1; i++) {
            result =
                result.with(
                    vertices[i],
                    new StraightLineEdge(vertices[i + 1])
                );
            result =
                result.with(
                    vertices[i + 1],
                    new StraightLineEdge(vertices[i])
                );
        }
        return result;
    }
}
