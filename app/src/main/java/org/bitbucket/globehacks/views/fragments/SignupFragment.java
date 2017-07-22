package org.bitbucket.globehacks.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.HomeActivity;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.presenters.SignupPresenter;
import org.bitbucket.globehacks.services.ApiService;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.interfaces.SignupView;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public class SignupFragment extends MvpFragment<SignupView, SignupPresenter> implements SignupView {

    private static final String TAG = SignupFragment.class.getSimpleName();
    private SweetAlertDialog loadingDialog;
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
    @BindView(R.id.cbx_signup_agreement)
    CheckBox cbx_signup_agreement;

    @Inject ApiService apiService;

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

        if (PreferencesManager.getObject(Keys.USER, User.class) != null)
            onSuccess();
    }

    @OnClick(R.id.btn_confirm)
    public void signup() {
        if(edt_contact_number.getText().toString().trim().equals("") || edt_email.getText().toString().trim().equals("") || edt_password.getText().toString().trim().equals("") || edt_firstname.getText().toString().trim().equals("") || edt_password.getText().toString().trim().equals("")){
            showWarningDialog();
        }else if(!cbx_signup_agreement.isChecked()){
            showWarningAgreementDialog();
        }else{
            showProgressDialog();
            presenter.register();
        }
    }

    @OnClick(R.id.btn_cancel)
    public void redirectToLogin() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, LoginFragment.newInstance(), LoginFragment.class.getSimpleName())
                .commit();
    }

    @NonNull
    @Override
    public SignupPresenter createPresenter() {
        return new SignupPresenter();
    }

    @Override
    public void onSuccess() {
        hideProgressDialog();
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    @Override
    public void onFailure() {
        hideProgressDialog();
        showErrorDialog();
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
    public String getMobile() {
        return edt_contact_number.getText().toString();
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


    private void showWarningAgreementDialog() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Invalid registration")
                .setContentText("You must agree with the Terms and Agreements")
                .setConfirmText("Close")
                .show();
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
                .setTitleText("Invalid registration")
                .setContentText("Either contact number or email already exist in our database")
                .setConfirmText("Close")
                .show();
    }

    private void showProgressDialog(){
        loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loadingDialog.setTitleText("Loading");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void hideProgressDialog(){
        loadingDialog.dismiss();
    }
}
