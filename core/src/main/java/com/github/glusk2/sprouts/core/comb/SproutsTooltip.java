package com.github.glusk2.sprouts.core.comb;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * This graph represents a visual UI tooltip that highlights the sprouts in
 * which the current move can end.
 * <p>
 * This graph is comprised of the sprouts that are on the boundary of a face
 * in which the current move can end.
 * <p>
 * {@link #edges()} returns an empty set.
 */
public final class SproutsTooltip implements SproutsGameState {
    @SuppressWarnings("checkstyle:javadocvariable")
    private static final int DEAD_SPROUT_DEGREE = 3;
    /** The state before the move. */
    private final SproutsGameState stateBeforeMove;
    /** The face in which the move is being drawn. */
    private final SproutsGameState face;
    /** The origin of the move. */
    private final Vertex moveOrigin;

    /**
     * Creates a new tooltip state by providing the move and state
     * before move.
     *
     * @param stateBeforeMove the state before the move
     * @param face the face in which the move is being drawn
     * @param moveOrigin the origin of the move
     */
    public SproutsTooltip(
        final SproutsGameState stateBeforeMove,
        final SproutsGameState face,
        final Vertex moveOrigin
    ) {
        this.stateBeforeMove = stateBeforeMove;
        this.face = face;
        this.moveOrigin = moveOrigin;
    }

    /**
     * Returns all living sprouts (with respect to {@code stateBeforeMove})
     * that are on the {@code face}.
     *
     * @return a set of tooltip sprouts in which a move can finnish
     */
    @Override
    public Set<Vertex> vertices() {
        return face.vertices().stream()
            .filter(v -> v.color().equals(Color.BLACK))
            .filter(v -> {
                Number blackDegree =
                    new VertexDegree(v, stateBeforeMove, Color.BLACK);
                return
                    !v.equals(moveOrigin) && blackDegree.intValue()
                        < DEAD_SPROUT_DEGREE
                 || v.equals(moveOrigin) && blackDegree.intValue()
                        < DEAD_SPROUT_DEGREE - 1;
            })
            .collect(Collectors.toSet());
    }

    /**
     * Returns an empty set.
     * <p>
     * Sprouts tooltip has no edges. It is only comprised of sprouts.
     *
     * @return an empty set
     */
    @Override
    public Set<SproutsEdge> edges() {
        return new HashSet<>();
    }

    /**
     * Renders {@link #vertices()}.
     *
     * @param renderer the renderer to render with
     * @param thickness the radius of vertices
     * @param circleSegmentCount the number of segments for the circles drawn
     * @param displayCobweb this argument has no effect on the rendering of
     *                      this graph
     */
    @Override
    public void render(
        final ShapeRenderer renderer,
        final float thickness,
        final int circleSegmentCount,
        final boolean displayCobweb
    ) {
        renderer.begin(ShapeType.Filled);
        renderer.setColor(Color.GREEN);
        for (Vertex v : vertices()) {
            renderer.circle(
                v.position().x,
                v.position().y,
                thickness,
                circleSegmentCount
            );
        }
        renderer.end();
    }
}
