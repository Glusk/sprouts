package com.github.glusk2.sprouts.core.geom;

import java.util.List;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents a curve in a 2-dimensional space as a list of splines.
 *
 * @param <T> A 2-dimensional spleen type object
 * @see com.badlogic.gdx.math.Bezier
 * @see com.badlogic.gdx.math.BSpline
 * @see com.badlogic.gdx.math.CatmullRomSpline
 */
public interface Curve<T extends Path<Vector2>> {
    /**
     * Returns this curve as a list of splines.
     *
     * @return the list of splines that represent this curve
     */
    List<T> splines();
}
