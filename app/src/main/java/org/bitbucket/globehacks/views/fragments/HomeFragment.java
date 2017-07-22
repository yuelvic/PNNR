package org.bitbucket.globehacks.views.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.truizlop.fabreveallayout.FABRevealLayout;

import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.presenters.HomePresenter;
import org.bitbucket.globehacks.views.interfaces.HomeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class HomeFragment extends MvpFragment<HomeView, HomePresenter> implements HomeView, MapboxMap.OnMapLongClickListener, MapboxMap.OnMyLocationChangeListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.map_view) MapView mapView;
    @BindView(R.id.geo_view) GeocoderAutoCompleteView geoView;

    private CameraPosition cameraPosition;
    private MapboxMap mapboxMap;

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;

    }

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

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            this.mapboxMap.setOnMapLongClickListener(this);
            this.mapboxMap.setOnMyLocationChangeListener(this);
            if (mapboxMap.getMyLocation() == null) return;

            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mapboxMap.getMyLocation()))
                    .build();

            mapboxMap.setCameraPosition(cameraPosition);
        });

        // Configure geolocation
        geoView.setAccessToken(Mapbox.getAccessToken());
        geoView.setType(GeocodingCriteria.TYPE_POI);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        presenter.release();
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        Log.d("Latlng", point.toString());

        mapboxMap.clear();
        mapboxMap.addMarker(new MarkerOptions().position(point).title("Dropped pin"));
    }

    @Override
    public void onMyLocationChange(@Nullable Location location) {
        if (location == null) return;
        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(location)).build());
    }
}
