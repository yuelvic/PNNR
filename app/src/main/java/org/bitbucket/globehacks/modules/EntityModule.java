package org.bitbucket.globehacks.modules;

import com.kinvey.android.Client;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

@Module
public class EntityModule {

    @Provides
    @Singleton
    Client client(GlobeHack globeHack) {
        return new Client.Builder(globeHack.getString(R.string.api_key_kinvey),
                globeHack.getString(R.string.api_secret_kinvey), globeHack).build();
    }

}
