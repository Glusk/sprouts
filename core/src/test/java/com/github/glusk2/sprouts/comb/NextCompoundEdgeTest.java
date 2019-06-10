package com.github.glusk2.sprouts.comb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

/** The test class for NextCompoundEdge. */
public final class NextCompoundEdgeTest {
    /**
     * Checks that a {@code NullPointerException} is thrown if the list of
     * LocalRotations does not contain key {@code current}.
     */
    public void throwsExceptionOnNullKey() {
        assertThat(
            new NextCompoundEdge(
                new HashMap<Vertex, LocalRotations>(),
                new CompoundEdge.Wrapped(
                    new StraightLineEdge(
                        new PresetVertex(Vector2.Zero, 0)
                    )
                )
            ).direction()
        ).isInstanceOf(NullPointerException.class);
    }

    /**
     * Checks that a {@code NullPointerException} is thrown if the list of
     * LocalRotations does not contain a mapping for key {@code current}.
     */
    public void throwsExceptionOnNullValue() {
        Map<Vertex, LocalRotations> rotationsList =
            new HashMap<Vertex, LocalRotations>();
        rotationsList.put(new PresetVertex(Vector2.Zero, 0), null);
        assertThat(
            new NextCompoundEdge(
                rotationsList,
                new CompoundEdge.Wrapped(
                    new StraightLineEdge(
                        new PresetVertex(Vector2.Zero, 0)
                    )
                )
            ).direction()
        ).isInstanceOf(NullPointerException.class);
    }
}
