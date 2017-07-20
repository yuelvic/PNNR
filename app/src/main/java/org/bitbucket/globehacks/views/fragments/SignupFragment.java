package org.bitbucket.globehacks.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.kinvey.android.Client;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.presenters.SignupPresenter;
import org.bitbucket.globehacks.views.interfaces.SignupView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class SignupFragment extends MvpFragment<SignupView, SignupPresenter> implements SignupView {

    private static final String TAG = SignupFragment.class.getSimpleName();

    @Inject Client kinveyClient;

    public static SignupFragment newInstance() {
        SignupFragment signupFragment = new SignupFragment();
        return signupFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.init();
        ((GlobeHack) getActivity().getApplication()).getEntityComponent().inject(this);
    }

    @NonNull
    @Override
    public SignupPresenter createPresenter() {
        return new SignupPresenter();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure() {

    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getUserType() {
        return null;
    }

    @Override
    public Client getKinveyClient() {
        return kinveyClient;
    }
}
