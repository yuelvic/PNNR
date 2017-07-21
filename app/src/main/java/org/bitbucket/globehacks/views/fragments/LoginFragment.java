package org.bitbucket.globehacks.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.presenters.LoginPresenter;
import org.bitbucket.globehacks.views.interfaces.LoginView;

import butterknife.ButterKnife;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class LoginFragment extends MvpFragment<LoginView, LoginPresenter> implements LoginView {

    private static final String TAG = LoginFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
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
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
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

}
