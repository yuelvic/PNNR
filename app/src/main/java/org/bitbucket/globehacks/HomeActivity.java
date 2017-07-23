package org.bitbucket.globehacks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.services.ApiService;
import org.bitbucket.globehacks.utils.FileManager;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.views.fragments.HomeFragment;
import org.bitbucket.globehacks.views.fragments.ProfileFragment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view) BottomNavigationView navigationView;

    @Inject ApiService apiService;

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        ((GlobeHack) getApplication()).getEntityComponent().inject(this);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            createFileFromBitmap(bitmap);
        } else {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                createFileFromBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void createFileFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();

            File file = FileManager.getOutputMediaFile();
            if (file == null) return;
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();

            Bitmap cBitmap = new Compressor(this)
                    .setMaxWidth(480)
                    .setQuality(85)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToBitmap(file);

            bos = new ByteArrayOutputStream();
            cBitmap.compress(Bitmap.CompressFormat.JPEG, 85, bos);
            data = bos.toByteArray();

            file = FileManager.getOutputMediaFile();
            if (file == null) return;
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();

            new FileManager().uploadImage(this, PreferencesManager.getObject(Keys.USER, User.class).getObjectId(),
                    file, new FileManager.UploadCallback() {
                        @Override
                        public void onSuccess(String url) {
                            try {
                                JSONObject jsonObject = new JSONObject(url);
                                updateUser(jsonObject.getString("fileURL"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure() {

                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(String avatarUrl) {
        User user = PreferencesManager.getObject(Keys.USER, User.class);
        user.setAvatar(avatarUrl);
        apiService.updateUser(user.getToken(), getString(R.string.api_key_application),
                getString(R.string.api_key_rest), user.getObjectId(), user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mUser -> {
                            PreferencesManager.remove(Keys.USER);
                            PreferencesManager.putObject(Keys.USER, user);
                            profileFragment.update();
                        },
                        Throwable::printStackTrace
                );
    }

    private void init() {
        int type = getIntent().getIntExtra("type", 2);
        switch (type) {
            case 0:
                profileFragment = ProfileFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, profileFragment, ProfileFragment.class.getSimpleName())
                        .commit();
                break;
            case 1:
                homeFragment = HomeFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, homeFragment, HomeFragment.class.getSimpleName())
                        .commit();
                break;
            default:
                homeFragment = HomeFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, homeFragment, HomeFragment.class.getSimpleName())
                        .commit();
                break;
        }
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
                        homeFragment = HomeFragment.newInstance(), HomeFragment.class.getSimpleName())
                .commit();
                return true;
            case R.id.action_profile:
                fragmentTransaction.replace(R.id.content_home,
                        profileFragment = ProfileFragment.newInstance(), ProfileFragment.class.getSimpleName())
                .commit();
                return true;
        }
        return false;
    }

}
