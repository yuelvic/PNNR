package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.dto.User;

import org.bitbucket.globehacks.views.interfaces.LoginView;

import java.io.IOException;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> implements KinveyClientCallback<User> {

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
    public void login() throws IOException {
        UserStore.login(mView.getUsername(), mView.getPassword(),
                mView.getKinveyClient(), this);
    }

    @Override
    public void onSuccess(User user) {
        mView.onSuccess();
    }

    @Override
    public void onFailure(Throwable throwable) {
        mView.onFailure();
    }
}
