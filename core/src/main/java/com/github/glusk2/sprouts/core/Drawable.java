package com.github.glusk2.sprouts.core;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Objects that implement this interface can be rendered. */
public interface Drawable {
    /**
     * Renders {@code this} object to {@code renderer}.
     *
     * @param renderer the renderer to draw with
     */
    void renderTo(ShapeRenderer renderer);
}
