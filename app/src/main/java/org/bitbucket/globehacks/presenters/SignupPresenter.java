package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.dto.User;

import org.bitbucket.globehacks.views.interfaces.EntityFields;
import org.bitbucket.globehacks.views.interfaces.SignupView;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class SignupPresenter extends MvpBasePresenter<SignupView> implements KinveyClientCallback<User> {

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
        UserStore.signUp(mView.getUsername(), mView.getPassword(),
                mView.getKinveyClient(), this);
    }

    /**
     * Puts additional user details to database
     */
    private void putAdditionalInfo() {
        mView.getKinveyClient().getActiveUser().put(EntityFields.USER_TYPE, mView.getUserType());
        mView.getKinveyClient().getActiveUser().put(EntityFields.USER_EMAIL, mView.getEmail());
        mView.getKinveyClient().getActiveUser().put(EntityFields.USER_FIRSTNAME, mView.getFirstName());
        mView.getKinveyClient().getActiveUser().put(EntityFields.USER_LASTNAME, mView.getLastName());
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
