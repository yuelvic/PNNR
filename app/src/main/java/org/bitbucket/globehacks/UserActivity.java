package org.bitbucket.globehacks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.bitbucket.globehacks.views.fragments.ChangePasswordFragment;
import org.bitbucket.globehacks.views.fragments.EditProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_user, EditProfileFragment.newInstance(), EditProfileFragment.TAG)
                .commit();

//        int type = getIntent().getIntExtra("", 0);
//
//        switch (type) {
//            case 1:
//                break;
//            case 2:
//                break;
//            default:
//                break;
//        }
    }

}
