package org.bitbucket.globehacks;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import org.bitbucket.globehacks.views.fragments.HomeFragment;
import org.bitbucket.globehacks.views.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view) BottomNavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
    }



    private void init() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home, HomeFragment.newInstance(), HomeFragment.class.getSimpleName())
                .commit();

        navigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.action_home:
                fragmentTransaction.replace(R.id.content_home,
                        HomeFragment.newInstance(), HomeFragment.class.getSimpleName())
                .commit();
                return true;
            case R.id.action_profile:
                fragmentTransaction.replace(R.id.content_home,
                        ProfileFragment.newInstance(), ProfileFragment.class.getSimpleName())
                .commit();
                return true;
        }
        return false;
    }

}
