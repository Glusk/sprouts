package com.github.glusk2.sprouts.core.comb;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * A CompoundEdge Polyline is a Polyline that prepends
 * {@link CompoundEdge#origin()} to {@link CompoundEdge#direction()}.
 */
public final class CompoundPolyline implements Polyline {
    /** The CompoundEdge to wrap. */
    private final CompoundEdge compoundEdge;

    /**
     * Constructs a new CompoundPolyline from {@code compoundEdge}.
     *
     * @param compoundEdge the CompoundEdge to wrap
     */
    public CompoundPolyline(final CompoundEdge compoundEdge) {
        this.compoundEdge = compoundEdge;
    }

    @Override
    public List<Vector2> points() {
        List<Vector2> result = new ArrayList<Vector2>();
        result.add(compoundEdge.origin().position());
        result.addAll(compoundEdge.direction().polyline().points());
        return result;
    }
}
