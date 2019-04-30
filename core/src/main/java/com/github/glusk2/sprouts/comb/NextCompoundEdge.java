package com.github.glusk2.sprouts.comb;

import java.util.Map;

/**
 * Represents the first CompoundEdge {@code v, (c, d)} after
 * {@code current - v, (a, b)} in {@code rotationsList}.
 * <p>
 * Refer to {@link LocalRotations} interface definition for more info on
 * notation used.
 * <p>
 * If {@code rotationsList} does not contain key {@code current.origin()} or
 * a mapping for key {@code current.origin()} is {@code null} NextCompoundEdge
 * does not exist and a {@code NullPointerException} is thrown on invocation of
 * {@code this.direction()}.
 */
public final class NextCompoundEdge implements CompoundEdge {
    /** The list of LocalRotations. */
    private final Map<Vertex, LocalRotations> rotationsList;
    /** The current CompoundEdge {@code v, (a, b)}. */
    private final CompoundEdge current;

    /**
     * Constructs a new CompoundEdge which is directly after {@code current}
     * in {@code rotationsList.get(current.origin())}.
     *
     * @param rotationsList the list of LocalRotations
     * @param current the current CompoundEdge {@code v, (a, b)}
     */
    public NextCompoundEdge(
        final Map<Vertex, LocalRotations> rotationsList,
        final CompoundEdge current
    ) {
        this.rotationsList = rotationsList;
        this.current = current;
    }

    @Override
    public Vertex origin() {
        return current.origin();
    }

    @Override
    public DirectedEdge direction() {
        return
            rotationsList.get(
                origin()
            ).next(
                current.direction()
            ).direction();
    }

    @Override
    public int hashCode() {
        return (
            origin().hashCode() + "-" + direction().hashCode()
        ).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof CompoundEdge)) {
            return false;
        }
        CompoundEdge that = (CompoundEdge) obj;
        return
            origin().equals(that.origin())
            && direction().equals(that.direction());
    }
}
