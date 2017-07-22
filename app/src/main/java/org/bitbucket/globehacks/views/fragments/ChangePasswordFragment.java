package org.bitbucket.globehacks.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.services.ApiService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by elthos on 7/22/17.
 */

public class ChangePasswordFragment extends Fragment {

    public static final String TAG = ChangePasswordFragment.class.getSimpleName();

    @BindView(R.id.edt_old_password) EditText edtOldPassword;
    @BindView(R.id.edt_new_password) EditText edtNewPassword;
    @BindView(R.id.edt_confirm_new_password) EditText edtConfirmNewPassword;

    @Inject ApiService apiService;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_change_password)
    public void onClickedChangedPassword() {

    }

    @OnClick(R.id.btn_cancel)
    public void onClickedCancel() {

    }
}
