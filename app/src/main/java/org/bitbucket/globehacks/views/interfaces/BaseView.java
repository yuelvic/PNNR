package org.bitbucket.globehacks.views.interfaces;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.bitbucket.globehacks.services.ApiService;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public interface BaseView extends MvpView {

    String getApplicationId();
    String getRestKey();

    ApiService getApiService();

}
