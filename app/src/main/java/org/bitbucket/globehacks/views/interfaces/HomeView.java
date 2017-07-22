package org.bitbucket.globehacks.views.interfaces;

import org.bitbucket.globehacks.models.User;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public interface HomeView extends BaseView {

    String getStoreName();
    double getStoreLatitude();
    double getStoreLongitude();
    boolean getStoreAvailability();
    String getStoreOperationTime();
    String getStoreOwner();

    User getProfile();

    void onAddedStoreSuccess();
    void onAddedStoreFailure();

}
