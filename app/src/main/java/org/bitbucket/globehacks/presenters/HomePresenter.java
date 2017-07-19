package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.bitbucket.globehacks.views.interfaces.HomeView;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class HomePresenter extends MvpBasePresenter<HomeView> {

    private static final String TAG = HomePresenter.class.getSimpleName();

    private HomeView mView;

    public void init() {
        mView = getView();
    }

    public void release() {

    }

}
