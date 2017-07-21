package org.bitbucket.globehacks;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import org.bitbucket.globehacks.components.DaggerNetworkComponent;
import org.bitbucket.globehacks.components.NetworkComponent;
import org.bitbucket.globehacks.modules.ApplicationModule;
import org.bitbucket.globehacks.modules.NetworkModule;

import timber.log.Timber;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class GlobeHack extends Application {

    private NetworkComponent entityComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize dependency injection
        entityComponent = DaggerNetworkComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(getString(R.string.api_base_url)))
                .build();

        // Plant a tree to get logs
        Timber.plant(new Timber.DebugTree());

        // Initialize map
        Mapbox.getInstance(this, getString(R.string.api_key_mapbox));
    }

    public NetworkComponent getEntityComponent() {
        return entityComponent;
    }

}
