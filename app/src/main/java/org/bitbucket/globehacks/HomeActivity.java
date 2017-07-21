package org.bitbucket.globehacks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.bitbucket.globehacks.views.fragments.HomeFragment;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home, HomeFragment.newInstance(), HomeFragment.class.getSimpleName())
                .commit();
    }
}
