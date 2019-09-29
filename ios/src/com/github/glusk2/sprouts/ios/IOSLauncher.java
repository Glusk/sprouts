package com.github.glusk2.sprouts.ios;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIScreen;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.github.glusk2.sprouts.core.Sprouts;

/** An iOS app main entry point. */
public final class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return
            new IOSApplication(
                new Sprouts(
                    (int) UIScreen.getMainScreen().getBounds().getWidth(),
                    (int) UIScreen.getMainScreen().getBounds().getHeight()
                ),
                config
            );
    }

    /**
     * IOS application main entry point.
     *
     * @param argv program arguments
     */
    public static void main(final String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}
