package com.github.glusk2.sprouts.core.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.comb.Graph;
import com.github.glusk2.sprouts.core.comb.SproutsGameState;

/**
 * A Snapshot is a "picture" of the game board.
 * <p>
 * Every touch event creates a new Snapshot of the game board. Every Snapshot
 * knows how to render itself to a {@code renderer}.
 */
public interface Snapshot {
    /**
     * Creates and returns a new Snapshot by updating {@code this} with the
     * {@code position} that the touch device detects on a "touch down" event.
     *
     * @param position the position detected on a "touch down" event in screen
     *                 coordinates
     * @return a new Snapshot created by updating {@code this} with
     *         {@code position}, considering the "touch down" event
     */
    Snapshot touchDown(Vector2 position);
    /**
     * Creates and returns a new Snapshot by updating {@code this} with the
     * {@code position} that the touch device detects on a "touch up" event.
     *
     * @param position the position detected on a "touch up" event in screen
     *                 coordinates
     * @return a new Snapshot created by updating {@code this} with
     *         {@code position}, considering the "touch up" event
     */
    Snapshot touchUp(Vector2 position);
    /**
     * Creates and returns a new Snapshot by updating {@code this} with the
     * {@code position} that the touch device detects on a "touch dragged"
     * event.
     *
     * @param position the position detected on a "touch dragged" event in
     *                 screen coordinates
     * @return a new Snapshot created by updating {@code this} with
     *         {@code position}, considering the "touch dragged" event
     */
    Snapshot touchDragged(Vector2 position);
    /**
     * Renders {@code this} "picture" (Snapshot) with {@code renderer}.
     *
     * @param renderer the graphics primitives rendering object
     */
    void render(ShapeRenderer renderer);

    /**
     * Returns the current state of the game board as Graph.
     *
     * @return the Graph representing the current game board state
     */
    Graph currentState();

    SproutsGameState gameState();
}
