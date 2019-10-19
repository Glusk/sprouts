package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;

/**
 * A tag for objects that have a color.
 * <p>
 * There are two colors that have a special meaning in a {@link Graph}:
 * <ul>
 *  <li><em>Red</em> objects are associated with the cobweb</li>
 *  <li><em>Black</em> objects are associated with sprouts and game Moves</li>
 * </ul>
 */
public interface ColorTag {
    /**
     * Returns the color of {@code this} object.
     *
     * @return the color of {@code this} object
     */
    Color color();
}
