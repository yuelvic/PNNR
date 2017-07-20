package org.bitbucket.globehacks;

import android.app.Application;

import com.kinvey.android.Client;
import com.mapbox.mapboxsdk.Mapbox;

import org.bitbucket.globehacks.components.DaggerEntityComponent;
import org.bitbucket.globehacks.components.EntityComponent;
import org.bitbucket.globehacks.modules.ApplicationModule;
import org.bitbucket.globehacks.modules.EntityModule;

import timber.log.Timber;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class GlobeHack extends Application {

    private EntityComponent entityComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize dependency injection
        entityComponent = DaggerEntityComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .entityModule(new EntityModule())
                .build();

        // Plant a tree to get logs
        Timber.plant(new Timber.DebugTree());

        // Initialize map
        Mapbox.getInstance(this, getString(R.string.api_key_mapbox));
    }

    public EntityComponent getEntityComponent() {
        return entityComponent;
    }

}
