package com.github.glusk2.sprouts.core.comb;

import static org.assertj.core.api.Assertions.assertThat;

import com.badlogic.gdx.math.Vector2;

import org.junit.Test;

/** Test class for ReversedCompoundEdge. */
public final class ReversedCompoundEdgeTest {
    /**
     * Checks that a CompoundEdge with {@code origin} and a single point
     * {@code direction} edge is properly reversed.
     */
    @Test
    public void reversesVertexToSinglePointLineEdge() {
        CompoundEdge actual =
            new ReversedCompoundEdge(
                new PresetVertex(
                    new Vector2(0, 0),
                    0
                ),
                new StraightLineEdge(
                    new PresetVertex(
                        new Vector2(1, 1),
                        1
                    )
                )
            );
        CompoundEdge expected =
            new CompoundEdge.Wrapped(
                new PresetVertex(
                    new Vector2(1, 1),
                    1
                ),
                new StraightLineEdge(
                    new PresetVertex(
                        new Vector2(0, 0),
                        0
                    )
                )
            );
        assertThat(actual.origin())
            .isEqualTo(expected.origin());
        assertThat(actual.direction().from())
            .isEqualTo(expected.direction().from());
        assertThat(actual.direction().to())
            .isEqualTo(expected.direction().to());
        assertThat(
            new ReversedCompoundEdge(
                new PresetVertex(
                    new Vector2(0, 0),
                    0
                ),
                new StraightLineEdge(
                    new PresetVertex(
                        new Vector2(1, 1),
                        1
                    )
                )
            )
        ).isEqualTo(
            new CompoundEdge.Wrapped(
                new PresetVertex(
                    new Vector2(1, 1),
                    1
                ),
                new StraightLineEdge(
                    new PresetVertex(
                        new Vector2(0, 0),
                        0
                    )
                )
            )
        );
    }
}
