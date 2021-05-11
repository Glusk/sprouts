package com.github.glusk2.sprouts.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** Sprouts main application class. */
public final class Sprouts extends Game {
    /**
     * Pre-set width of the game surface that contains the UI bar and the
     * drawing surface, in pixels.
     * <p>
     * If the game window resizes to a higher resolution, the game stretches.
     * <p>
     * If the game window resizes to a smaller resolution, the game shrinks.
     */
    private static final int GAME_WIDTH = 640;
    /**
     * Pre-set height of the game surface that contains the UI bar and the
     * drawing surface, in pixels.
     * <p>
     * If the game window resizes to a higher resolution, the game stretches.
     * <p>
     * If the game window resizes to a smaller resolution, the game shrinks.
     */
    private static final int GAME_HEIGHT = 480;
    /**
     * If either the width or the height of the screen are smaller than
     * this constant, the screen is considered "small".
     */
    private static final int SMALL_SCREEN_LIMIT = 600;
    /**
     * Normal screen size divisor
     * <p>
     * This is used to compute the move thickness. The minimum game board
     * dimension is divided by this constant to produce the move thickness
     * on normal screens.
     */
    private static final float NORMAL_SCREEN_DIVISOR = 60;
    /**
     * Small screen size divisor
     * <p>
     * This is used to compute the move thickness. The minimum game board
     * dimension is divided by this constant to produce the move thickness
     * on small screens.
     */
    private static final float SMALL_SCREEN_DIVISOR = 35;
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

        float minDimensionRatio = NORMAL_SCREEN_DIVISOR;
        if (Math.min(screenWidth, screenHeight) < SMALL_SCREEN_LIMIT) {
            minDimensionRatio = SMALL_SCREEN_DIVISOR;
        }
        this.setScreen(
            new MainScreen(
                this,
                new FitViewport(GAME_WIDTH, GAME_HEIGHT),
                minDimensionRatio,
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
