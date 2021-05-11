package com.github.glusk2.sprouts.core.comb;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.github.glusk2.sprouts.core.util.Check;

/**
 * A check that tests whether a Move is possible in the {@code face} of
 * the {@code gameState}.
 */
public final class IsSubmovePossibleInFace implements Check {
    /** Maximum sprout lives. */
    private static final int MAXIMUM_SPROUT_LIVES = 3;

    /** Specifies whether the {@code origin()} of the Submove is a sprout. */
    private final boolean isOriginSprout;
    /** The graph that the {@code face} belongs to. */
    private final SproutsGameState gameState;
    /** A Set of edges that comprise a face of the {@code gameState}. */
    private final Set<SproutsEdge> face;

    /**
     * Creates a new Check by specifying the {@code gameState} and the
     * {@code face}.
     * <p>
     * This constructor assumes that the origin of the Submove <em>is</em> a
     * sprout.
     *
     * @param gameState the graph that the {@code face} belongs to
     * @param face a Set of edges that comprise a face of the
     *             {@code gameState}
     */
    public IsSubmovePossibleInFace(
        final SproutsGameState gameState,
        final Set<SproutsEdge> face
    ) {
        this(
            true,
            gameState,
            face
        );
    }
    /**
     * Creates a new Check by specifying the {@code gameState} and the
     * {@code face}.
     * <p>
     * This constructor does not assume whether the Submove origin is a
     * sprout or not - {@code isOriginSprout} specifies that.
     *
     * @param isOriginSprout specifies whether the of the
     *                       Submove is a sprout
     * @param gameState the graph that the {@code face} belongs to
     * @param face a Set of edges that comprise a face of the
     *             {@code gameState}
     */
    public IsSubmovePossibleInFace(
        final boolean isOriginSprout,
        final SproutsGameState gameState,
        final Set<SproutsEdge> face
    ) {
        this.isOriginSprout = isOriginSprout;
        this.gameState = gameState;
        this.face = face;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if {@code face} is not part of the
     *                                  {@code gameState}
     */
    @Override
    public boolean check() {
        if (!new SproutsFaces(gameState.edges()).faces().contains(face)) {
            throw new IllegalArgumentException(
                "\"face\" is not part of the \"gameState\"!"
            );
        }
        Set<Vertex> faceVertices = new HashSet<Vertex>();
        for (SproutsEdge edge : face) {
            faceVertices.add(edge.from());
            faceVertices.add(edge.to());
        }

        int faceLives = 0;
        for (Vertex v : faceVertices) {
            if (v.color().equals(Color.BLACK)) {
                faceLives +=
                    MAXIMUM_SPROUT_LIVES
                  - new VertexDegree(v, gameState, Color.BLACK).intValue();
            }
        }
        if (isOriginSprout) {
            return faceLives >= 2;
        }
        return faceLives >= 1;
    }
}
