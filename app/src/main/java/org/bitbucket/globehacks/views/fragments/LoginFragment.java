package org.bitbucket.globehacks.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.HomeActivity;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.presenters.LoginPresenter;
import org.bitbucket.globehacks.services.ApiService;
import org.bitbucket.globehacks.views.interfaces.LoginView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class LoginFragment extends MvpFragment<LoginView, LoginPresenter> implements LoginView {

    private static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.edt_email) EditText etEmail;
    @BindView(R.id.edt_password) EditText etPassword;

    @Inject ApiService apiService;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

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

    @OnClick(R.id.btn_login)
    public void login() {
        if (etEmail.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("")) {
            showWarningDialog();
        } else {
            presenter.login();
        }
    }

    @OnClick(R.id.btn_login_signup)
    public void redirectToSignup() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, SignupFragment.newInstance(), SignupFragment.class.getSimpleName())
                .commit();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    @Override
    public void onFailure() {
        showErrorDialog();
    }

    @Override
    public String getEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
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


    private void showWarningDialog() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("All fields required")
                .setContentText("Please fill up all fields")
                .setConfirmText("Close")
                .show();
    }

    private void showErrorDialog() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Invalid Credentials")
                .setContentText("Either username or password is incorrect")
                .setConfirmText("Close")
                .show();
    }

}
