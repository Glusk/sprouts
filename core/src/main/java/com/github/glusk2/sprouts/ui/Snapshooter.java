package com.github.glusk2.sprouts.ui;

/**
 * A Snapshooter is a "camera" that takes "pictures" (Snapshots).
 */
public interface Snapshooter {
    /**
     * Creates and returns a new "picture" (Snapshot).
     *
     * @return a new Snapshot
     */
    Snapshot snapshot();
}
