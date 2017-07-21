package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.bitbucket.globehacks.views.interfaces.SignupView;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class SignupPresenter extends MvpBasePresenter<SignupView> {

    private static final String TAG = SignupPresenter.class.getSimpleName();

    private SignupView mView;

    public void init() {
        mView = getView();
    }

    public void release() {

    }

    /**
     * Register user
     */
    public void register() {
    }

    /**
     * Puts additional user details to database
     */
    private void putAdditionalInfo() {
    }

}
