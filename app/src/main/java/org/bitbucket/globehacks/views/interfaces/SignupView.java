package org.bitbucket.globehacks.views.interfaces;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public interface SignupView extends BaseView {

    void onSuccess();
    void onFailure();

    String getUsername();
    String getPassword();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getUserType();

}
