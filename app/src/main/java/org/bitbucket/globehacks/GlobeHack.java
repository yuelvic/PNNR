package org.bitbucket.globehacks;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import timber.log.Timber;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class GlobeHack extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Plant a tree to get logs
        Timber.plant(new Timber.DebugTree());

        // Initialize map
        Mapbox.getInstance(this, getString(R.string.api_key_mapbox));
    }
}
