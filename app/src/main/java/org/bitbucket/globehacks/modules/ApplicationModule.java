package org.bitbucket.globehacks.modules;

import org.bitbucket.globehacks.GlobeHack;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

@Module
public class ApplicationModule {

    private GlobeHack globeHack;

    public ApplicationModule(GlobeHack globeHack) {
        this.globeHack = globeHack;
    }

    @Provides
    @Singleton
    GlobeHack globeHack() {
        return globeHack;
    }

}
