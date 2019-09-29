package com.github.glusk2.sprouts.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.glusk2.sprouts.core.Sprouts;

/** A desktop application main entry point. */
public final class DesktopLauncher {
    /** Private constructor prevents instantiation. */
    private DesktopLauncher() {
    }

    /**
     * Desktop application main entry point.
     *
     * @param arg program arguments
     */
    public static void main(final String[] arg) {
        LwjglApplicationConfiguration config =
            new LwjglApplicationConfiguration();
        config.useHDPI = true;
        config.samples = 2;
        new LwjglApplication(
            new Sprouts(
                LwjglApplicationConfiguration.getDesktopDisplayMode().width,
                LwjglApplicationConfiguration.getDesktopDisplayMode().height
            ),
            config
        );
    }
}
