package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public interface Polyline {
    List<Vector2> points();

    final class SAMPLE_WRAPPER implements Polyline {

        private final List<Vector2> sample;

        public SAMPLE_WRAPPER(List<Vector2> sample) {
            this.sample = sample;
        }

        @Override
        public List<Vector2> points() {
            return sample;
        }
    }
}
