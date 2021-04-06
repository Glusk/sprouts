package com.github.glusk2.sprouts.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** Sprouts main application class. */
public final class Sprouts extends Game {
    /** The number of sprouts displayed in the game shown on app start. */
    private static final int NUM_OF_PRESET_SPROUTS = 2;
    /** Application screen width in pixels. */
    private final int screenWidth;
    /** Application screen height in pixels. */
    private final int screenHeight;
    /**
     * A single {@code ShapeRenderer} that is used by all {@code Game} screens.
     * <p>
     * A new instance is created in {@code this.create()} and destroyed in
     * {@code this.dispose()}.
     */
    private ShapeRenderer renderer;

    /**
     * Creates a new Game by specifying the screen dimensions.
     *
     * @param worldWidth  Application screen width in pixels
     * @param worldHeight Application screen height in pixels
     */
    public Sprouts(final int worldWidth, final int worldHeight) {
        this.screenWidth = worldWidth;
        this.screenHeight = worldHeight;
    }

    @Override
    public void create() {
        renderer = new ShapeRenderer();
        Gdx.graphics.setContinuousRendering(false);
        this.setScreen(
            new MainScreen(
                this,
                new FitViewport(screenWidth, screenHeight),
                renderer,
                NUM_OF_PRESET_SPROUTS
            )
        );
    }

    @Override
    public void dispose() {
        this.getScreen().hide();
        renderer.dispose();
    }
}
