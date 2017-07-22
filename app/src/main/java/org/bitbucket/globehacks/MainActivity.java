package org.bitbucket.globehacks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.fragments.HomeFragment;
import org.bitbucket.globehacks.views.fragments.LoginFragment;
import org.bitbucket.globehacks.views.fragments.SignupFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LOCATION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        if (PreferencesManager.getObject(Keys.USER, User.class) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, LoginFragment.newInstance(), LoginFragment.class.getSimpleName())
                    .commit();
        } else {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        if (checkLocationPermission())
            requestLocationPermission();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.LOCATION}, LOCATION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: show current location on map
                }
                break;
        }
    }
}
