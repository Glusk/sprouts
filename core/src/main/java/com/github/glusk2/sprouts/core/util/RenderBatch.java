package com.github.glusk2.sprouts.core.util;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Objects of {@code this} type represent a {@link ShapeRenderer}
 * <em>batch</em>.
 * <p>
 * Every {@code render()} implementation must begin with
 * {@code renderer.begin()} and end with {@code renderer.end()}.
 */
public interface RenderBatch {
    /**
     * Renders {@code this} <em>batch</em>.
     *
     * @param renderer the object to render {@code this} <em>batch</em> to
     */
    void render(ShapeRenderer renderer);
}
