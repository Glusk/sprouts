package com.github.glusk2.sprouts.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** The Sprouts game board. */
public final class GameBoard extends Actor {
    /** The thickness of {@code this} game board border. */
    private static final float BORDER_THICKNESS = 5f;

    /** The default background color of {@code this} game board. */
    private static final Color DEFAULT_BACKGROUND_COLOR =
        new Color(204 / 255f, 229 / 255f, 1, 1);

    /**
     * The {@code ShapeRenderer} object used to draw {@code this} game
     * board.
     */
    private final ShapeRenderer renderer;

    /** The "camera" of {@code this} GameBoard. */
    private Snapshooter camera;

    /**
     * Constructs a new {@code GameBoard} by specifying the {@code renderer}
     * and the {@code camera}.
     *
     * @param renderer the {@code ShapeRenderer} object used to draw
     *                 {@code this} game board
     * @param camera the "camera" of {@code this} GameBoard
     */
    public GameBoard(
        final Snapshooter camera,
        final ShapeRenderer renderer
    ) {
        this.camera = camera;
        this.renderer = renderer;
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.end();

        Gdx.gl.glLineWidth(BORDER_THICKNESS);
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeType.Line);
        renderer.setColor(DEFAULT_BACKGROUND_COLOR);
        renderer.rect(getX(), getY(), getWidth(), getHeight());
        renderer.end();

        camera.snapshot().render(renderer);

        batch.begin();
    }
}
