package com.github.glusk2.sprouts.core.geom;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * Straight line segments connecting neighbouring {@link #points()} in a
 * 2-dimensional space.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Polygonal_chain">Polygonal chain</a>
 */
public interface Polyline {

    /**
     * Returns the vertices of {@code this} polyline.
     *
     * @return the list of points on {@code this} polyline
     */
    List<Vector2> points();


    /**
     * A wrapper class for a list of points.
     * <p>
     * Changes made to the array or a list passed through the constructor
     * reflect in {@link #points()}.
     */
    final class WrappedList implements Polyline {

        /** The points on this polyline. */
        private final List<Vector2> wrappedPoints;

        /**
         * Creates a new polyline from an array of points.
         *
         * @param pointsToWrap the points on this polyline
         */
        public WrappedList(final Vector2... pointsToWrap) {
            this(Arrays.asList(pointsToWrap));
        }
        /**
         * Creates a new polyline from a list of points.
         *
         * @param pointsToWrap the points on this polyline
         */
        public WrappedList(final List<Vector2> pointsToWrap) {
            this.wrappedPoints = pointsToWrap;
        }

        /** {@inheritDoc} */
        @Override
        public List<Vector2> points() {
            return wrappedPoints;
        }
    }
}
