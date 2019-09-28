package com.github.glusk2.sprouts.core.ui;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.comb.Graph;
import com.github.glusk2.sprouts.core.comb.InitialCobweb;
import com.github.glusk2.sprouts.core.comb.Vertex;

/**
 * This Snapshot represents the game board <em>before</em> a Move is being
 * drawn.
 * <p>
 * A "touch down" event initiates a new Move, the other touch events return
 * {@code this}.
 */
public final class BeforeMove implements Snapshot {
    /** The thickness of the Moves drawn. */
    private final float moveThickness;
    /** The number of segments used to draw circular Vertices. */
    private final int circleSegmentCount;
    /** The Graph that a Move will be drawn to. */
    private final Graph currentState;
     /** Any Submove that is drawn outside of {@code gameBounds} is invalid. */
    private final Rectangle gameBounds;

    /**
     * Creates a new Snapshot, using the default initial state.
     *
     * @param moveThickness the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     */
    public BeforeMove(
        final float moveThickness,
        final int circleSegmentCount,
        final Rectangle gameBounds
    ) {
        this(
            new InitialCobweb(
                moveThickness,
                circleSegmentCount
            ),
            moveThickness,
            circleSegmentCount,
            gameBounds
        );
    }

    /**
     * Creates a new Snapshot, using the specified Graph -
     * {@code currentState}.
     *
     * @param currentState the Graph that a Move will be drawn to
     * @param moveThickness the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     */
    public BeforeMove(
        final Graph currentState,
        final float moveThickness,
        final int circleSegmentCount,
        final Rectangle gameBounds
    ) {
        this.currentState = currentState;
        this.moveThickness = moveThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.gameBounds = gameBounds;
    }

    @Override
    public Snapshot touchDown(final Vector2 position) {
        for (Vertex v : currentState.vertices()) {
            if (
                v.position().dst(position) < 2 * moveThickness
             && currentState.isAliveSprout(v)
            ) {
                return
                    new MoveDrawing(
                        currentState,
                        moveThickness,
                        circleSegmentCount,
                        v,
                        new LinkedList<Vector2>(),
                        gameBounds
                    );
            }
        }
        return this;
    }

    @Override
    public Snapshot touchUp(final Vector2 position) {
        return this;
    }

    @Override
    public Snapshot touchDragged(final Vector2 position) {
        return this;
    }

    @Override
    public void render(final ShapeRenderer renderer) {
        currentState.renderTo(renderer);
    }
}
