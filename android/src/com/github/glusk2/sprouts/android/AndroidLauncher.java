package com.github.glusk2.sprouts.android;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.glusk2.sprouts.core.Sprouts;

/** An android app main entry point. */
public final class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config =
            new AndroidApplicationConfiguration();
        config.numSamples = 2;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        initialize(
            new Sprouts(
                displayMetrics.widthPixels,
                displayMetrics.heightPixels
            ),
            config
        );
    }
}
