package org.bitbucket.globehacks.views.interfaces;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public interface LoginView extends BaseView {

    void onSuccess();
    void onFailure();

    String getEmail();
    String getPassword();

}
