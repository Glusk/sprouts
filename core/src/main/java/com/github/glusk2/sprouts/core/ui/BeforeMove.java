package com.github.glusk2.sprouts.core.ui;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.comb.Graph;
import com.github.glusk2.sprouts.core.comb.GraphCreation;
import com.github.glusk2.sprouts.core.comb.IsMovePossible;
import com.github.glusk2.sprouts.core.comb.SproutsGameState;
import com.github.glusk2.sprouts.core.comb.SproutsInitialState;
import com.github.glusk2.sprouts.core.comb.TransformedGraph;
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
    private final SproutsGameState gameState;
     /** Any Submove that is drawn outside of {@code gameBounds} is invalid. */
    private final Rectangle gameBounds;

    /**
     * Creates a new Snapshot, using the default initial state.
     *
     * @param moveThickness the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param numOfSprouts the number of starting sprouts to generate
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     */
    public BeforeMove(
        final float moveThickness,
        final int circleSegmentCount,
        final int numOfSprouts,
        final Rectangle gameBounds
    ) {
        this(
            new SproutsInitialState(
                numOfSprouts,
                gameBounds
            ),
            moveThickness,
            circleSegmentCount,
            gameBounds
        );
    }

    /**
     *
     * @param moveThickness the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     */
    public BeforeMove(
        final SproutsGameState gameState,
        final float moveThickness,
        final int circleSegmentCount,
        final Rectangle gameBounds
    ) {
        this.gameState = gameState;
        this.moveThickness = moveThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.gameBounds = gameBounds;
    }

    @Override
    public Snapshot touchDown(final Vector2 position) {
        /*
        if (!new IsMovePossible(currentState).check()) {
            return this;
        }
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
        }*/
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

    // Todo: delete old API
    @Override
    public Graph currentState() {
        return null;
    }

    @Override
    public void render(final ShapeRenderer renderer) {
        SproutsGameState gameState = this.gameState();
        // currentState.render(renderer);
        gameState.render(renderer, moveThickness, circleSegmentCount);
        /*if (!new IsMovePossible(currentState).check()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            Color overlayColor = Color.WHITE.cpy();
            overlayColor.a = 1 / 2f;

            renderer.begin(ShapeType.Filled);
            renderer.setColor(overlayColor);
            renderer.rect(
                gameBounds.getX(),
                gameBounds.getY(),
                gameBounds.getWidth(),
                gameBounds.getHeight()
            );
            renderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        */
    }

    @Override
    public SproutsGameState gameState() {
        return this.gameState;
    }
}
