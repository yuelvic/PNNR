package org.bitbucket.globehacks.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.shawnlin.preferencesmanager.PreferencesManager;
import com.tmxlr.lib.driodvalidatorlight.Form;
import com.tmxlr.lib.driodvalidatorlight.helper.Range;
import com.tmxlr.lib.driodvalidatorlight.helper.RegexTemplate;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.HomeActivity;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.presenters.EditProfilePresenter;
import org.bitbucket.globehacks.services.ApiService;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.interfaces.EditProfileView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by elthos on 7/22/17.
 */

public class EditProfileFragment extends MvpFragment<EditProfileView, EditProfilePresenter> implements EditProfileView {

    public static final String TAG = EditProfileFragment.class.getSimpleName();

    @BindView(R.id.edt_contact_number) EditText edtContactNumber;
    @BindView(R.id.edt_email) EditText edtEmail;
    @BindView(R.id.edt_firstname) EditText edtFirstName;
    @BindView(R.id.edt_lastname) EditText edtLastName;

    @Inject ApiService apiService;
    @Inject Form form;

    private SweetAlertDialog loadingDialog;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @NonNull
    @Override
    public EditProfilePresenter createPresenter() {
        return new EditProfilePresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((GlobeHack) getActivity().getApplication()).getEntityComponent().inject(this);
        presenter.initialize();
        initialize();
    }



    private void initialize() {
        form.check(edtContactNumber, RegexTemplate.NOT_EMPTY_PATTERN, "Required Field");
        form.checkLength(edtContactNumber, Range.equal(11), "Must be exactly 11 digits");
//        form.check(edtEmail, RegexTemplate.EMAIL_PATTERN, "Must be a valid email address");
        form.check(edtFirstName, RegexTemplate.NOT_EMPTY_PATTERN, "Required Field");
        form.check(edtLastName, RegexTemplate.NOT_EMPTY_PATTERN, "Required Field");
    }

    @OnClick(R.id.btn_update_profile)
    public void onClickedUpdate() {
       if (form.validate()) {
           showProgressDialog();

           presenter.passInputs(edtFirstName.getText().toString(), edtLastName.getText().toString(), edtEmail.getText().toString(),
                   edtContactNumber.getText().toString());
       }
    }

    @OnClick(R.id.btn_cancel)
    public void onClickedCancel() {
        getActivity().finish();
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
    public String getApplicationId() {
        return getString(R.string.api_key_application);
    }

    @Override
    public String getRestKey() {
        return getString(R.string.api_key_rest);
    }

    @Override
    public void onSuccess() {
        hideProgressDialog();

        Toast.makeText(getContext(), "Profile successfully changed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onFailure() {
        hideProgressDialog();

        Toast.makeText(getContext(), "Changing unsuccessful", Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog(){
        loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loadingDialog.setTitleText("Loading. Please wait...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void hideProgressDialog(){
        loadingDialog.dismiss();
    }
}
