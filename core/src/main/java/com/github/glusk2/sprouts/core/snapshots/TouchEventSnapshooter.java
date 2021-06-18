package com.github.glusk2.sprouts.core.snapshots;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * A TouchEventSnapshooter is a special kind of "camera" that takes "pictures"
 * of the game board state on every touch event.
 * <p>
 * This is a mutable object that re-directs InputListener's {@code touch}
 * events to the encapsulated {@code initialSnapshot} object.
 * <p>
 * {@code snapshot()} returns the updated {@code initialSnapshot} after zero
 * or more {@code touch} events.
 */
public final class TouchEventSnapshooter
    extends InputListener implements Snapshooter {

    /** The updated {@code initialSnapshot} after zero or more touch events. */
    private Snapshot currentSnapshot;

    /**
     * Creates a new TouchEventSnapshooter by specifying the
     * {@code initialSnapshot}.
     *
     * @param initialSnapshot the initial Snapshot
     */
    public TouchEventSnapshooter(final Snapshot initialSnapshot) {
        this.currentSnapshot = initialSnapshot;
    }

    @Override
    public Snapshot snapshot() {
        return currentSnapshot;
    }

    @Override
    public boolean touchDown(
        final InputEvent event,
        final float x,
        final float y,
        final int pointer,
        final int button
    ) {
        currentSnapshot =
            currentSnapshot.touchDown(
                event.getTarget().localToStageCoordinates(new Vector2(x, y))
            );
        return true;
    }

    @Override
    public void touchUp(
        final InputEvent event,
        final float x,
        final float y,
        final int pointer,
        final int button
    ) {
        currentSnapshot =
            currentSnapshot.touchUp(
                event.getTarget().localToStageCoordinates(new Vector2(x, y))
            );
    }

    @Override
    public void touchDragged(
        final InputEvent event,
        final float x,
        final float y,
        final int pointer
    ) {
        currentSnapshot =
            currentSnapshot.touchDragged(
                event.getTarget().localToStageCoordinates(new Vector2(x, y))
            );
    }
}
