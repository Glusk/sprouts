package com.github.glusk2.sprouts.core.comb;

/**
 * A decorator for CompoundEdge that caches {@code original}.
 * <p>
 * Not thread-safe!
 */
public final class CachedCompoundEdge implements CompoundEdge {
    /** The CompoundEdge that {@code this} object is a cache of. */
    private final CompoundEdge original;

    /** Cached origin of {@code original}. */
    private Vertex origin;
    /** Cached direction of {@code original}. */
    private DirectedEdge direction;

    /**
     * Constructs a new CachedCompoundEdge by wrapping {@code origin} and
     * {@code direction}.
     *
     * @param origin the origin Vertex {@code v}
     * @param direction the direction DirectedEdge {@code (a, b)}
     */
    public CachedCompoundEdge(
        final Vertex origin,
        final DirectedEdge direction
    ) {
        this(new CompoundEdge.Wrapped(origin, direction));
    }

    /**
     * Constructs a new CachedCompoundEdge from the specified CompoundEdge to
     * cache.
     *
     * @param original the CompoundEdge {@code v, (a, b)} that {@code this}
     *                 object is a cache of
     */
    public CachedCompoundEdge(final CompoundEdge original) {
        this.original = original;
    }

    @Override
    public Vertex origin() {
        if (origin == null) {
            origin = original.origin();
        }
        return origin;
    }

    @Override
    public DirectedEdge direction() {
        if (direction == null) {
            direction = original.direction();
        }
        return direction;
    }

    @Override
    public int hashCode() {
        return new CompoundEdge.Wrapped(origin(), direction()).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return new CompoundEdge.Wrapped(origin(), direction()).equals(obj);
    }
}
