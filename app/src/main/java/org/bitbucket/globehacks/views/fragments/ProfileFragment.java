package org.bitbucket.globehacks.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.HomeActivity;
import org.bitbucket.globehacks.MainActivity;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.UserActivity;
import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.presenters.ProfilePresenter;
import org.bitbucket.globehacks.services.ApiService;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.interfaces.ProfileView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public class ProfileFragment extends MvpFragment<ProfileView, ProfilePresenter> implements ProfileView {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.civ_profile_image) CircleImageView civProfileImage;
    @BindView(R.id.tv_fullname) TextView tvFullName;
    @BindView(R.id.tv_number) TextView tvNumber;
    @BindView(R.id.tv_user_type) TextView tvUserType;

    @Inject ApiService apiService;

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.init();
        ((GlobeHack) getActivity().getApplication()).getEntityComponent().inject(this);

        initialize();
    }

    private void initialize() {
//        civProfileImage.setImageResource();
        tvFullName.setText(getUser().getFirstname() + " " + getUser().getLastname());
        tvNumber.setText(getUser().getMobile());
        tvUserType.setText(getUser().getType());
    }

    @OnClick(R.id.civ_profile_image)
    public void changeProfilePicture() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Change profile picture")
                .setMessage("Choose from..")
                .setNeutralButton("Camera", (dialog, which) -> {

                })
                .setNeutralButton("Gallery", (dialog, which) -> {

                })
                .show();
    }

    @OnClick(R.id.btn_profile_edit)
    public void onClickEdit() {
        startActivity(new Intent(getActivity(), UserActivity.class));
    }

    @OnClick(R.id.btn_profile_preferences)
    public void onClickProfilePref() {

    }

    @OnClick(R.id.btn_profile_change_password)
    public void onClickChangedPassword() {

    }

    @OnClick(R.id.btn_profile_logout)
    public void onProfileLogout() {
        presenter.logout();
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @Override
    public String getApplicationId() {
        return getString(R.string.api_key_application);
    }

    @Override
    public String getRestKey() {
        return getString(R.string.api_key_rest);
    }

    @Override
    public ApiService getApiService() {
        return apiService;
    }

    @Override
    public User getUser() {
        return PreferencesManager.getObject(Keys.USER, User.class);
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onFailure() {

    }


}
