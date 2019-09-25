package com.github.glusk2.sprouts;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** Sprouts main application class. */
public final class Sprouts extends Game {

    /** Application default world width in pixels. */
    private static final int DEFAULT_WIDTH = 1280;
    /** Application default world height in pixels. */
    private static final int DEFAULT_HEIGHT = 720;

    /**
     * A single {@code ShapeRenderer} that is used by all {@code Game} screens.
     * <p>
     * A new instance is created in {@code this.create()} and destroyed in
     * {@code this.dispose()}.
     */
    private ShapeRenderer renderer;

    @Override
    public void create() {
        renderer = new ShapeRenderer();
        this.setScreen(
            new MainScreen(
                this,
                new FitViewport(DEFAULT_WIDTH, DEFAULT_HEIGHT),
                renderer
            )
        );
    }

    @Override
    public void dispose() {
        this.getScreen().hide();
        renderer.dispose();
    }
}
