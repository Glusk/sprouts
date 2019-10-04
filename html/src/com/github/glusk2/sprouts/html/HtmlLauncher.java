package com.github.glusk2.sprouts.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.github.glusk2.sprouts.core.Sprouts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

/** An HTML5 application entry point. */
public final class HtmlLauncher extends GwtApplication {

    // USE THIS CODE FOR A FIXED SIZE APPLICATION
    // @Override
    // public GwtApplicationConfiguration getConfig () {
    //         return new GwtApplicationConfiguration(480, 320);
    // }
    // END CODE FOR FIXED SIZE APPLICATION

    // UNCOMMENT THIS CODE FOR A RESIZABLE APPLICATION
    // PADDING is to avoid scrolling in iframes, set to 20 if you have
    // problems
    /** The padding of the browser window. */
    private static final int PADDING = 0;

    @Override
    public GwtApplicationConfiguration getConfig() {
        int w = Window.getClientWidth() - PADDING;
        int h = Window.getClientHeight() - PADDING;
        GwtApplicationConfiguration cfg =
            new GwtApplicationConfiguration(w, h);
        Window.enableScrolling(false);
        Window.setMargin("0");
        Window.addResizeHandler(new ResizeListener());
        cfg.preferFlash = false;

        cfg.antialiasing = true;
        return cfg;
    }

    /** A window resize handler. */
    class ResizeListener implements ResizeHandler {
        @Override
        public void onResize(final ResizeEvent event) {
            int width = event.getWidth() - PADDING;
            int height = event.getHeight() - PADDING;
            getRootPanel().setWidth("" + width + "px");
            getRootPanel().setHeight("" + height + "px");
            getApplicationListener().resize(width, height);
            Gdx.graphics.setWindowedMode(width, height);
        }
    }
    // END OF CODE FOR RESIZABLE APPLICATION

    @Override
    public Preloader.PreloaderCallback getPreloaderCallback() {
        return
            createPreloaderPanel(
                GWT.getHostPageBaseURL() + "logo_preload.png"
            );
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new Sprouts(
            Window.getClientWidth(),
            Window.getClientHeight()
        );
    }
}
