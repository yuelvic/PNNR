package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.bitbucket.globehacks.views.interfaces.LoginView;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private static final String TAG = "LoginPresenter";

    private LoginView mView;

    public void init() {
        mView = getView();
    }

    public void release() {

    }

    /**
     * Authenticate user
     */
    public void login() {
    }

}
