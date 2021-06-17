package com.github.glusk2.sprouts.core;

/**
 * A mutable cobweb switch.
 * <p>
 * This class may be used to track whether the user wants to display the
 * cobweb edges and vertices.
 * <p>
 * When the switch is <strong>ON</strong>, the cobweb is displayed. When the
 * switch is <strong>OFF</strong>, the cobweb is hidden.
 */
public final class CobwebSwitch {
    /**
     * If {@code true}, the cobweb switch is <strong>ON</strong>, else it is
     * <strong>OFF</strong>.
     */
    private boolean isEnabled;

    /**
     * Creates a new switch by pre-setting the state.
     *
     * @param isEnabled if {@code true}, the cobweb switch is
     *                  <strong>ON</strong>, else it is
     *                  <strong>OFF</strong>
     */
    public CobwebSwitch(final boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Returns the current state of this switch.
     *
     * @return if {@code true}, the cobweb switch is <strong>ON</strong>,
     *         else it is <strong>OFF</strong>
     */
    public boolean state() {
        return isEnabled;
    }

    /** Toggles the current state of this switch. */
    public void toggle() {
        isEnabled = !isEnabled;
    }
}
