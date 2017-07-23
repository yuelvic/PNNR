package org.bitbucket.globehacks.views.interfaces;

import org.bitbucket.globehacks.models.GeoPoint;

import java.util.List;
import org.bitbucket.globehacks.models.Store;
import org.bitbucket.globehacks.models.GeoPoint;
import org.bitbucket.globehacks.models.User;

import java.util.List;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public interface HomeView extends BaseView {

    double getStoreLatitude();
    double getStoreLongitude();
    boolean getStoreAvailability();
    String getStoreName();
    String getStoreOperationTime();
    String getStoreOwner();
    String getContactNumber();

    double getMapNWLatitude();
    double getMapNWLongitude();
    double getMapSELatitude();
    double getMapSELongitude();

    User getProfile();

    void onAddedStoreSuccess();
    void onAddedStoreFailure();

    void onGetStoreSuccess(Store store);
    void onGetStoreFailure();

    void onGeoPointLoadSuccess(List<GeoPoint> geoPoints);
    void onGeoPointLoadFailure();
}
