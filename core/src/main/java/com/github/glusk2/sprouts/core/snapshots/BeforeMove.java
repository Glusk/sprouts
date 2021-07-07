package com.github.glusk2.sprouts.core.snapshots;

import java.util.Arrays;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.glusk2.sprouts.core.ToggleSwitch;
import com.github.glusk2.sprouts.core.comb.IsAliveSprout;
import com.github.glusk2.sprouts.core.comb.IsMovePossible;
import com.github.glusk2.sprouts.core.comb.NearestSproutSearch;
import com.github.glusk2.sprouts.core.comb.SproutsGameState;
import com.github.glusk2.sprouts.core.comb.SproutsInitialState;
import com.github.glusk2.sprouts.core.comb.Vertex;

/**
 * This Snapshot represents the game board <em>before</em> a Move is being
 * drawn.
 * <p>
 * A "touch down" event initiates a new Move, the other touch events return
 * {@code this}.
 */
public final class BeforeMove implements Snapshot {
    /** The graph representing the current game board state. */
    private final SproutsGameState gameState;
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
     * Creates a new Snapshot, using the default initial state.
     *
     * @param moveThickness the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param numOfSprouts the number of starting sprouts to generate
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     * @param displayCobweb a switch that tracks whether the player wishes to
     *                      display cobweb
     * @param playerTurn A switch that tracks the player turn. If ON, it's
     *                   "Player 1"'s turn, else it is "Player 2"'s'.
     * @param playerTurnLabel a reference to the UI label to update player turns
     */
    public BeforeMove(
        final float moveThickness,
        final int circleSegmentCount,
        final int numOfSprouts,
        final Rectangle gameBounds,
        final ToggleSwitch displayCobweb,
        final ToggleSwitch playerTurn,
        final Label playerTurnLabel
    ) {
        this(
            new SproutsInitialState(
                numOfSprouts,
                gameBounds
            ),
            moveThickness,
            circleSegmentCount,
            gameBounds,
            displayCobweb,
            playerTurn,
            playerTurnLabel
        );
    }

    /**
     * Creates a new Snapshot, using the set game state.
     *
     * @param gameState the graph representing the current game board state
     * @param moveThickness the thickness of the Moves drawn
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
    public BeforeMove(
        final SproutsGameState gameState,
        final float moveThickness,
        final int circleSegmentCount,
        final Rectangle gameBounds,
        final ToggleSwitch displayCobweb,
        final ToggleSwitch playerTurn,
        final Label playerTurnLabel
    ) {
        this.gameState = gameState;
        this.moveThickness = moveThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.gameBounds = gameBounds;
        this.displayCobweb = displayCobweb;
        this.playerTurn = playerTurn;
        this.playerTurnLabel = playerTurnLabel;
    }

    @Override
    public Snapshot touchDown(final Vector2 position) {
        if (!new IsMovePossible(gameState).check()) {
            return this;
        }

        Vertex nearest =
            new NearestSproutSearch(
                gameState,
                position,
                2 * moveThickness,
                Color.BLACK
            ).result();

        if (new IsAliveSprout(gameState).test(nearest)) {
            return
                new MoveDrawing(
                    gameState,
                    moveThickness,
                    circleSegmentCount,
                    nearest,
                    new LinkedList<Vector2>(Arrays.asList(nearest.position())),
                    gameBounds,
                    displayCobweb,
                    playerTurn,
                    playerTurnLabel
                );
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
    @SuppressWarnings("checkstyle:avoidinlineconditionals")
    public void render(final ShapeRenderer renderer) {
        gameState.render(
            renderer,
            moveThickness,
            circleSegmentCount,
            displayCobweb.state()
        );
        if (!new IsMovePossible(gameState).check()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            Color overlayColor = Color.WHITE.cpy();
            overlayColor.a = 1 / 2f;

            playerTurnLabel.setColor(Color.GREEN);
            playerTurnLabel.setText(
                "Player " + (playerTurn.state() ? 1 : 2) + " wins!"
            );

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
    }

    @Override
    public SproutsGameState gameState() {
        return this.gameState;
    }
}
