package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.bitbucket.globehacks.views.interfaces.ProfileView;

import rx.Subscription;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public class ProfilePresenter extends MvpBasePresenter<ProfileView> {

    private static final String TAG = ProfilePresenter.class.getSimpleName();

    private ProfileView mView;

    private Subscription profileSubscription;

    public void init() {
        mView = getView();
    }

    public void release() {
        checkProfileSubscription();
    }

    private void checkProfileSubscription() {
        if (profileSubscription != null && profileSubscription.isUnsubscribed())
            profileSubscription.unsubscribe();
    }

    public void getProfile() {

    }

}
