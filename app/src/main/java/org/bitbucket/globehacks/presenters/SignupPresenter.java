package org.bitbucket.globehacks.presenters;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.interfaces.SignupView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class SignupPresenter extends MvpBasePresenter<SignupView> {

    private static final String TAG = SignupPresenter.class.getSimpleName();

    private SignupView mView;

    private Subscription signupSubscription;

    public void init() {
        mView = getView();
    }

    public void release() {
        checkSignupSubscription();
    }

    private void checkSignupSubscription() {
        if (signupSubscription != null && signupSubscription.isUnsubscribed())
            signupSubscription.unsubscribe();
    }

    /**
     * Creates user from form
     * @return User
     */
    private User createUser() {
        User user = new User();
        user.setEmail(mView.getEmail());
        user.setFirstname(mView.getFirstName());
        user.setLastname(mView.getLastName());
        user.setPassword(mView.getPassword());
        user.setMobile(mView.getMobile());
        user.setType("owner");

        return user;
    }

    /**
     * Register user
     */
    public void register() {
        checkSignupSubscription();
        signupSubscription = mView.getApiService()
                .register(mView.getApplicationId(), mView.getRestKey(), createUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            PreferencesManager.putObject(Keys.USER, user);
                            mView.onSuccess();
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            mView.onFailure();
                        }
                );
    }

    /**
     * Puts additional user details to database
     */
    private void putAdditionalInfo() {
    }

}
