package com.github.glusk2.sprouts.core.geom;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.util.RenderBatch;

/** A Polyline RenderBatch. */
public final class PolylineBatch implements RenderBatch {
    /** The Polyline to render. */
    private final Polyline polyline;
    /** The color of the {@code polyline}. */
    private final Color color;
    /** The thickness of the polyline line segments. */
    private final float lineThickness;
    /** The number of segments used to draw {@code polyline} vertex points. */
    private final int circleSegmentCount;
    /**
     * A flag that specifies whether {@code this} RenderBatch is being
     * rendered in a parent batch.
     */
    private final boolean isNestedBatch;

    /**
     * Constructs a new PolylineBatch.
     *
     * @param polyline the Polyline to render
     * @param color the color of the {@code polyline}
     * @param lineThickness the thickness of the polyline line segments
     * @param circleSegmentCount the number of segments used to draw
     *                           {@code polyline} vertex points
     * @param isNestedBatch a flag that specifies whether {@code this}
     *                      RenderBatch is being rendered in a parent batch
     */
    public PolylineBatch(
        final Polyline polyline,
        final Color color,
        final float lineThickness,
        final int circleSegmentCount,
        final boolean isNestedBatch
    ) {
        this.polyline = polyline;
        this.color = color;
        this.lineThickness = lineThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.isNestedBatch = isNestedBatch;
    }

    @Override
    public void render(final ShapeRenderer renderer) {
        if (!isNestedBatch) {
            renderer.begin(ShapeType.Filled);
        }
        renderer.setColor(color);
        List<Vector2> points = polyline.points();
        for (int i = 0; i < points.size(); i++) {
            Vector2 p2 = points.get(i);
            renderer.circle(
                p2.x,
                p2.y,
                lineThickness / 2,
                circleSegmentCount
            );
            if (i > 0) {
                Vector2 p1 = points.get(i - 1);
                renderer.rectLine(p1, p2, lineThickness);
            }
        }
        if (!isNestedBatch) {
            renderer.end();
        }
    }
}
