package org.bitbucket.globehacks.components;

import org.bitbucket.globehacks.modules.ApplicationModule;
import org.bitbucket.globehacks.modules.NetworkModule;
import org.bitbucket.globehacks.views.fragments.LoginFragment;
import org.bitbucket.globehacks.views.fragments.SignupFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface NetworkComponent {

    void inject(SignupFragment signupFragment);
    void inject(LoginFragment loginFragment);

}
