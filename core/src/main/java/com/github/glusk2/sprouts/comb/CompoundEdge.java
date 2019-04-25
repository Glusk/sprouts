package com.github.glusk2.sprouts.comb;

/**
 * A CompoundEdge is an edge in LocalRotations. It starts in {@code origin()},
 * which is considered to be connected to {@code direction().from()} with a
 * straight line.
 * <p>
 * Refer to {@link LocalRotations} interface definition for more info on
 * notation used.
 */
public interface CompoundEdge {
    /**
     * Returns the origin of {@code this} CompoundEdge.
     *
     * @return center vertex {@code v}
     */
    Vertex origin();

    /**
     * Returns the direction of {@code this} CompoundEdge.
     *
     * @return edge {@code (a, b)}
     */
    DirectedEdge direction();

    /** A simple origin and direction CompoundEdge wrapper. */
    final class Wrapped implements CompoundEdge {
        /** The origin Vertex {@code v}. */
        private final Vertex origin;
        /** The direction DirectedEdge {@code (a, b)}. */
        private final DirectedEdge direction;

        /**
         * Constructs a new CompoundEdge from {@code edge}.
         * <p>
         * {@code edge} is treated as a straight line.
         * <p>
         * Equivalent to:
         * <pre>
         * new CompoundEdge.Wrapped(
         *     edge.from(),
         *         new StraightLineEdge(
         *             edge.color(),
         *             edge.to()
         *         )
         *     )
         * );
         * </pre>
         *
         * @param edge a DirectedEdge, viewed as a straight line
         */
        public Wrapped(final DirectedEdge edge) {
            this(
                edge.from(),
                new StraightLineEdge(
                    edge.color(),
                    edge.to()
                )
            );
        }

        /**
         * Constructs a new CompoundEdge by wrapping {@code origin} and
         * {@code direction}.
         *
         * @param origin the origin Vertex {@code v}
         * @param direction the direction DirectedEdge {@code (a, b)}
         */
        public Wrapped(final Vertex origin, final DirectedEdge direction) {
            this.origin = origin;
            this.direction = direction;
        }

        @Override
        public Vertex origin() {
            return origin;
        }

        @Override
        public DirectedEdge direction() {
            return direction;
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
}
