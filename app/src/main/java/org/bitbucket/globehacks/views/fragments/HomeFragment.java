package org.bitbucket.globehacks.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.mapbox.mapboxsdk.maps.MapView;

import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.presenters.HomePresenter;
import org.bitbucket.globehacks.views.interfaces.HomeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class HomeFragment extends MvpFragment<HomeView, HomePresenter> implements HomeView {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.map_view) MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.init();
    }

    @Override
    public void onStop() {
        presenter.release();
        super.onStop();
    }

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }

}
