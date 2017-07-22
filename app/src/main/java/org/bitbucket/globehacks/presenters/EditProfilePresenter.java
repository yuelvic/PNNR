package org.bitbucket.globehacks.presenters;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.shawnlin.preferencesmanager.PreferencesManager;
import com.tmxlr.lib.driodvalidatorlight.Form;

import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.interfaces.EditProfileView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by elthos on 7/22/17.
 */

public class EditProfilePresenter extends MvpBasePresenter<EditProfileView> {

    private static final String TAG = EditProfilePresenter.class.getName();

    private EditProfileView mView;

    public void initialize() {
        mView = getView();
    }

    public void passInputs(String firstName, String lastName, String email, String contactNumber) {
        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setMobile(contactNumber);

        Log.d(TAG, mView.getUser().getObjectId() + " " + mView.getUser().getToken());

        mView.getApiService()
                .updateUser(mView.getApplicationId(), mView.getRestKey(), mView.getUser().getToken(),
                        mView.getApplicationId(), mView.getRestKey(), mView.getUser().getObjectId(), user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user1 -> {
                            PreferencesManager.putObject(Keys.USER, User.class);
                            mView.onSuccess();
                        },

                        throwable -> {
                            throwable.printStackTrace();
                            mView.onSuccess();
                        });
    }

}
