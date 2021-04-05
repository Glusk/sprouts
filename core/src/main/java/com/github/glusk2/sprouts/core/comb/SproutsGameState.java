package com.github.glusk2.sprouts.core.comb;

import java.util.Set;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface SproutsGameState {
    Set<SproutsEdge> edges();
    // TODO: make this method default
    void render(
        ShapeRenderer renderer,
        float lineThickness,
        int circleSegmentCount
    );
}
