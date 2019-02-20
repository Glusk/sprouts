package com.github.glusk2.sprouts.geom;

import java.util.List;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

/**
 * A {@code Curve} <em>decorator</em> that caches the original list of splines.
 */
public final class CachedCurve implements Curve<Path<Vector2>> {

    /** The original curve. */
    private final Curve<Path<Vector2>> source;

    /** The cached list of splines. */
    private List<Path<Vector2>> cached;

    /**
     * Creates a new {@code CachedCurve} from {@code source}.
     *
     * @param source the curve to cache
     */
    public CachedCurve(final Curve<Path<Vector2>> source) {
        this.source = source;
    }

    /**
     * Fetches the original list of splines only once and caches it. On
     * consecutive invocations the cached list is returned instead.
     * <p>
     * This implementation is not <em>thread-safe</em>.
     *
     * @return the cached list of splines
     */
    @Override
    public List<Path<Vector2>> splines() {
        if (cached == null) {
            cached = source.splines();
        }
        return cached;
    }
}
