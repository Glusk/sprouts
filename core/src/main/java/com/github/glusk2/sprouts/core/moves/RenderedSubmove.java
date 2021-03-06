package com.github.glusk2.sprouts.core.moves;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.glusk2.sprouts.core.geom.PolylineBatch;
import com.github.glusk2.sprouts.core.util.RenderBatch;

/** Represents a Submove drawn on the screen. */
public final class RenderedSubmove implements RenderBatch {
    /** The wrapped Submove to draw. */
    private final Submove submove;
    /** The thickness of the line drawn. */
    private final float lineThickness;
    /**
     * The number of segments for the circles between adjacent line
     * segments.
     */
    private final int circleSegmentCount;
    /**
     * A flag that specifies whether this Submove is being rendered in a
     * parent batch as part of a Move.
     */
    private final boolean isNestedBatch;
    /** The color to paint this submove with. */
    private final Color submovePaint;

    /**
     * Constructs a new RenderedSubmove from the Submove and the rendering
     * settings.
     *
     * @param submove the wrapped Submove to draw
     * @param lineThickness the thickness of the line drawn
     * @param circleSegmentCount the number of segments for the circles between
     *                           adjacent line segments
     * @param isNestedBatch a flag that specifies whether this Submove is being
     *                      rendered in parent batch as part of a Move
     * @param submovePaint the color to paint this submove with
     */
    public RenderedSubmove(
        final Submove submove,
        final float lineThickness,
        final int circleSegmentCount,
        final boolean isNestedBatch,
        final Color submovePaint
    ) {
        this.submove = submove;
        this.lineThickness = lineThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.isNestedBatch = isNestedBatch;
        this.submovePaint = submovePaint;
    }

    @Override
    public void render(final ShapeRenderer renderer) {
        if (!submove.isReadyToRender()) {
            return;
        }

        if (!isNestedBatch) {
            renderer.begin(ShapeType.Filled);
        }

        Color polylineColor = submovePaint;
        new PolylineBatch(
            submove.asEdge().polyline(),
            polylineColor,
            lineThickness,
            circleSegmentCount,
            isNestedBatch
        ).render(renderer);

        if (!isNestedBatch) {
            renderer.end();
        }
    }
}
