package com.github.glusk2.sprouts.core;

import java.util.Iterator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** A RenderedMove renders the move by rendering all of its Submoves. */
public final class RenderedMove implements Drawable {
    /** The wrapped Move to draw. */
    private final Move move;
    /** The thickness of the line drawn. */
    private final float lineThickness;
    /**
     * The number of segments for the circles between adjacent line
     * segments.
     */
    private final int circleSegmentCount;

    /**
     * Constructs a new RenderedMove from {@code move} and the rendering
     * settings.
     *
     * @param move the wrapped Move to draw
     * @param lineThickness the thickness of the line drawn
     * @param circleSegmentCount the number of segments for the circles between
     *                           adjacent line segments
     */
    public RenderedMove(
        final Move move,
        final float lineThickness,
        final int circleSegmentCount
    ) {
        this.move = move;
        this.lineThickness = lineThickness;
        this.circleSegmentCount = circleSegmentCount;
    }

    @Override
    public void renderTo(final ShapeRenderer renderer) {
        Iterator<Submove> it = move.iterator();
        while (it.hasNext()) {
            Submove next = it.next();
            new RenderedSubmove(
                next,
                lineThickness,
                circleSegmentCount
            ).renderTo(renderer);
            it = next;
        }
    }
}
