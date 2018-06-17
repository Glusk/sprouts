package com.github.glusk2.sprouts.geom;

import java.util.List;

public final class CachedCurve<T> implements Curve {

    private final Curve source;

    private List<T> cached;

    public CachedCurve(Curve source) {
        this.source = source;
    }
    @Override
    public List<T> splines() {
        if (cached == null) {
            cached = source.splines();
        }
        return cached;
    }
}
