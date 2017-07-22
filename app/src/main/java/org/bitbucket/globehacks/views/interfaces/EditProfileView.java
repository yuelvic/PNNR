package org.bitbucket.globehacks.views.interfaces;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.services.ApiService;

/**
 * Created by elthos on 7/22/17.
 */

public interface EditProfileView extends MvpView {

    ApiService getApiService();

    User getUser();

    String getApplicationId();

    String getRestKey();

    void onSuccess();

    void onFailure();

}
