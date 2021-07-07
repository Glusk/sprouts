package com.github.glusk2.sprouts.core.snapshots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.glusk2.sprouts.core.ToggleSwitch;
import com.github.glusk2.sprouts.core.comb.SproutsGameState;
import com.github.glusk2.sprouts.core.comb.SproutsStateAfterMove;
import com.github.glusk2.sprouts.core.moves.MiddleSprout;
import com.github.glusk2.sprouts.core.moves.Move;
import com.github.glusk2.sprouts.core.moves.RenderedMove;

/**
 * This Snapshot represents the game board <em>after</em> a Move is drawn,
 * indicating that a sprout can be added anywhere on the Move.
 * <p>
 * A "touch up" event detects the position of a new sprout and adds it to the
 * last drawn Move.
 */
public final class SproutAdd implements Snapshot {
    /** The graph that a Move is being drawn to. */
    private final SproutsGameState currentState;
    /** The move to place the middle sprout on. */
    private final Move move;
    /** The thickness of the Moves drawn. */
    private final float moveThickness;
    /** The number of segments used to draw circular Vertices. */
    private final int circleSegmentCount;
    /** Any Submove that is drawn outside of {@code gameBounds} is invalid. */
    private final Rectangle gameBounds;
    /** A switch that tracks whether the player wishes to display cobweb. */
    private final ToggleSwitch displayCobweb;
    /**
     * A switch that tracks the player turn. If ON, it's "Player 1"'s turn,
     * else it is "Player 2"'s'.
     */
    private final ToggleSwitch playerTurn;
    /** A reference to the UI label to update player turns. */
    private final Label playerTurnLabel;

    /**
     * Creates a new SproutAdd Snapshot from the {@code currentState},
     * {@code move}.
     *
     * @param currentState the graph that a Move is being drawn to
     * @param move the move to place the middle sprout on
     * @param moveThickness  the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     * @param displayCobweb a switch that tracks whether the player wishes to
     *                      display cobweb
     * @param playerTurn A switch that tracks the player turn. If ON, it's
     *                   "Player 1"'s turn, else it is "Player 2"'s'.
     * @param playerTurnLabel a reference to the UI label to update player turns
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public SproutAdd(
        final SproutsGameState currentState,
        final Move move,
        final float moveThickness,
        final int circleSegmentCount,
        final Rectangle gameBounds,
        final ToggleSwitch displayCobweb,
        final ToggleSwitch playerTurn,
        final Label playerTurnLabel
    ) {
        this.currentState = currentState;
        this.move = move;
        this.moveThickness = moveThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.gameBounds = gameBounds;
        this.displayCobweb = displayCobweb;
        this.playerTurn = playerTurn;
        this.playerTurnLabel = playerTurnLabel;
    }

    @Override
    public Snapshot touchDown(final Vector2 position) {
        return this;
    }

    @Override
    @SuppressWarnings("checkstyle:avoidinlineconditionals")
    public Snapshot touchUp(final Vector2 position) {
        MiddleSprout middleSprout =
            new MiddleSprout(move, position, moveThickness);

        if (middleSprout.submove() != null) {
            playerTurn.toggle();
            if (playerTurnLabel != null) {
                playerTurnLabel.setText(
                    "Player " + (playerTurn.state() ? 2 : 1) + " on the move!"
                );
                Gdx.graphics.requestRendering();
            }
        }
        return
            new BeforeMove(
                new SproutsStateAfterMove(
                    currentState,
                    move,
                    middleSprout
                ),
                moveThickness,
                circleSegmentCount,
                gameBounds,
                displayCobweb,
                playerTurn,
                playerTurnLabel
            );
}

    @Override
    public Snapshot touchDragged(final Vector2 position) {
        return this;
    }

    @Override
    public void render(final ShapeRenderer renderer) {
        new RenderedMove(
            move,
            moveThickness,
            circleSegmentCount
        ).render(renderer);
        currentState.render(
            renderer,
            moveThickness,
            circleSegmentCount,
            displayCobweb.state()
        );
    }

    @Override
    public SproutsGameState gameState() {
        return this.currentState;
    }
}
