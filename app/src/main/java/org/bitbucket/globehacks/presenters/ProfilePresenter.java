package org.bitbucket.globehacks.presenters;

import android.util.Log;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.interfaces.ProfileView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public void logout() {
        checkProfileSubscription();
        profileSubscription = mView.getApiService()
                .logout(mView.getApplicationId(), mView.getRestKey(), mView.getUser().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        o -> {

                        },
                        Throwable::printStackTrace,
                        () -> {}
                );
        PreferencesManager.remove(Keys.USER);
        mView.onSuccess();
    }
}
