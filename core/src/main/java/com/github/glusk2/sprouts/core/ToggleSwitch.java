package com.github.glusk2.sprouts.core;

/** A mutable toggle switch. */
public final class ToggleSwitch {
    /**
     * If {@code true}, the toggle switch is <strong>ON</strong>, else it is
     * <strong>OFF</strong>.
     */
    private boolean isEnabled;

    /**
     * Creates a new switch by pre-setting the state.
     *
     * @param isEnabled if {@code true}, the toggle switch is
     *                  <strong>ON</strong>, else it is
     *                  <strong>OFF</strong>
     */
    public ToggleSwitch(final boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Returns the current state of this switch.
     *
     * @return if {@code true}, the toggle switch is <strong>ON</strong>,
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
