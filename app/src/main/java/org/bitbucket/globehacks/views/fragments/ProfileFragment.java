package org.bitbucket.globehacks.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.GlobeHack;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getPhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivityForResult(takePictureIntent, 1);
        }
    }

    private void getPhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    public void update() {
        Glide.with(getActivity()).load(getUser().getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(civProfileImage);
    }

    private void initialize() {
        Glide.with(getActivity()).load(getUser().getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_default)
                .into(civProfileImage);
        tvFullName.setText(getUser().getFirstname() + " " + getUser().getLastname());
        tvNumber.setText(getUser().getMobile());
        tvUserType.setText(getUser().getType());
    }

    @OnClick(R.id.civ_profile_image)
    public void changeProfilePicture() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Choose from..")
                .setItems(R.array.photo_mode, (dialog, which) -> {
                    if (which == 0) getPhotoFromCamera();
                    else getPhotoFromGallery();
                })
                .create()
                .show();
    }

    @OnClick(R.id.btn_profile_edit)
    public void onClickEdit() {
        startActivity(new Intent(getActivity(), UserActivity.class));
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
        Log.e("Logout","Success");
    }

    @Override
    public void onFailure() {
        Log.e("Logout","Failed");

    }


}
