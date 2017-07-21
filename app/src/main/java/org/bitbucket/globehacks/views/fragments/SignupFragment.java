package org.bitbucket.globehacks.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.presenters.SignupPresenter;
import org.bitbucket.globehacks.views.interfaces.SignupView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class SignupFragment extends MvpFragment<SignupView, SignupPresenter> implements SignupView {

    private static final String TAG = SignupFragment.class.getSimpleName();
    @BindView(R.id.edt_contact_number)
    EditText edt_contact_number;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_firstname)
    EditText edt_firstname;
    @BindView(R.id.edt_lastname)
    EditText edt_lastname;
    @BindView(R.id.spn_usertype)
    Spinner spn_usertype;
    @BindView(R.id.btn_signup_confirm)
    Button btn_signup_confirm;
    @BindView(R.id.btn_signup_cancel)
    Button getBtn_signup_cancel;


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
        return edt_contact_number.getText().toString();
    }

    @Override
    public String getPassword() {
        return edt_password.getText().toString();
    }

    @Override
    public String getEmail() {
        return edt_email.getText().toString();
    }

    @Override
    public String getFirstName() {
        return edt_firstname.getText().toString();
    }

    @Override
    public String getLastName() {
        return edt_lastname.getText().toString();
    }

    @Override
    public String getUserType() {
        return spn_usertype.getSelectedItem().toString();
    }

}
