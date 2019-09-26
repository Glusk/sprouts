package com.github.glusk2.sprouts.geom;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * A PolylinePiece is a piece of the {@code original} Polyline, that is cut in
 * two at the {@code cuttingPoint}.
 * <p>
 * {@code pieceFlag} indicates which half this PolylinePiece represents
 * ({@code true} - the first half, {@code false} - the second half).
 * <p>
 * If the {@code cuttingPoint} is not on the {@code original} Polyline,
 * {@link #points()} returns an empty list.
 */
public final class PolylinePiece implements Polyline {
    /** The default value for {@code segmentIntersectError}. */
    private static final float DEFAULT_SEGMENT_INTERSECT_ERROR = .5f;
    /** The Polyline to cut. */
    private final Polyline original;
    /** The point at which to cut {@code original}. */
    private final Vector2 cuttingPoint;
    /**
     * A flag that indicates which half this PolylinePiece represents
     * ({@code true} - the first half, {@code false} - the second half).
     */
    private final boolean pieceFlag;
    /**
     * Maximum error margin for detection of intersection between a polyline
     * segment and {@code point}.
     */
    private final float segmentIntersectError;

    /**
     * Constructs <strong>the second half</strong> PolylinePiece from
     * {@code original} that is cut at the {@code cuttingPoint}, using the
     * default segment intersect error.
     * <p>
     * Equivalent to:
     * <pre>
     * new PolylinePiece(original, cuttingPoint, false);
     * </pre>
     *
     * @param original the Polyline to cut
     * @param cuttingPoint the point at which to cut {@code original}
     */
    public PolylinePiece(final Polyline original, final Vector2 cuttingPoint) {
        this(original, cuttingPoint, false, DEFAULT_SEGMENT_INTERSECT_ERROR);
    }

    /**
     * Constructs the specified PolylinePiece from {@code original} that is
     * cut at the {@code cuttingPoint}.
     *
     * @param original the Polyline to cut
     * @param cuttingPoint the point at which to cut {@code original}
     * @param pieceFlag a flag that indicates which half this PolylinePiece
     *                  represents ({@code true} - the first half,
     *                  {@code false} - the second half)
     * @param segmentIntersectError maximum error margin for detection of
     *                              intersection between a polyline segment and
     *                              {@code point}
     */
    public PolylinePiece(
        final Polyline original,
        final Vector2 cuttingPoint,
        final boolean pieceFlag,
        final float segmentIntersectError
    ) {
        this.original = original;
        this.cuttingPoint = cuttingPoint;
        this.pieceFlag = pieceFlag;
        this.segmentIntersectError = segmentIntersectError;
    }

    @Override
    public List<Vector2> points() {
        List<Vector2> points = new ArrayList<Vector2>(original.points());
        for (int i = 1; i < points.size(); i++) {
            Vector2 p0 = points.get(i - 1).cpy();
            Vector2 p1 = points.get(i).cpy();
            if (
                new IsPointOnLineSegment(
                    p0,
                    p1,
                    cuttingPoint,
                    segmentIntersectError
                ).check()
            ) {
                if (pieceFlag) {
                    List<Vector2> result = points.subList(0, i);
                    result.add(cuttingPoint);
                    return result;
                } else {
                    List<Vector2> result = new ArrayList<Vector2>();
                    result.add(cuttingPoint);
                    result.addAll(points.subList(i, points.size()));
                    return result;
                }
            }
        }
        return new ArrayList<Vector2>();
    }
}
