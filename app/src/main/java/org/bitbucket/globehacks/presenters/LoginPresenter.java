package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.interfaces.LoginView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private static final String TAG = "LoginPresenter";

    private LoginView mView;

    private Subscription loginSubscription;

    public void init() {
        mView = getView();
    }

    public void release() {
        checkLoginSubscription();
    }

    private void checkLoginSubscription() {
        if (loginSubscription != null && loginSubscription.isUnsubscribed())
            loginSubscription.unsubscribe();
    }

    private User createUser() {
        User user = new User();
        user.setLogin(mView.getEmail());
        user.setPassword(mView.getPassword());
        return user;
    }

    /**
     * Authenticate user
     */
    public void login() {
        checkLoginSubscription();
        loginSubscription = mView.getApiService()
                .login(mView.getApplicationId(), mView.getRestKey(), createUser())
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

}
