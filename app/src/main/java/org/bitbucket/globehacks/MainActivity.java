package org.bitbucket.globehacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.bitbucket.globehacks.views.fragments.HomeFragment;
import org.bitbucket.globehacks.views.fragments.SignupFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, HomeFragment.newInstance(), HomeFragment.class.getSimpleName())
                .commit();
    }
}
