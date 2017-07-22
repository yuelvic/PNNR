package org.bitbucket.globehacks.views.interfaces;

import org.bitbucket.globehacks.models.User;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public interface ProfileView extends BaseView {

    User getUser();

    void onSuccess();
    void onFailure();

}
