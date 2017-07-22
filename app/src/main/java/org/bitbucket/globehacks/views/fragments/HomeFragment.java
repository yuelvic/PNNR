package org.bitbucket.globehacks.views.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.v5.MapboxNavigation;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.commons.models.Position;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.presenters.HomePresenter;
import org.bitbucket.globehacks.services.ApiService;
import org.bitbucket.globehacks.utils.Utilities;
import org.bitbucket.globehacks.views.interfaces.HomeView;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class HomeFragment extends MvpFragment<HomeView, HomePresenter> implements HomeView, MapboxMap.OnMapLongClickListener, MapboxMap.OnMyLocationChangeListener, GeocoderAutoCompleteView.OnFeatureListener, View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.view_home) View homeView;
    @BindView(R.id.map_view) MapView mapView;
    @BindView(R.id.geo_view) GeocoderAutoCompleteView geoView;
    @BindView(R.id.fab_action) FloatingActionButton fabStore;

    @BindView(R.id.view_add_store) View addStoreView;

    @Inject ApiService apiService;

    private CameraPosition cameraPosition;
    private MapboxMap mapboxMap;
    private MapboxNavigation navigation;

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
        ((GlobeHack) getActivity().getApplication()).getEntityComponent().inject(this);
        presenter.init();

        fabStore.setOnClickListener(this);

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

        navigation = new MapboxNavigation(getContext(), Mapbox.getAccessToken());

        // Configure geolocation
        geoView.setAccessToken(Mapbox.getAccessToken());
        geoView.setType(GeocodingCriteria.TYPE_POI);
        geoView.setCountry("PH");
        geoView.setOnFeatureListener(this);
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

    /**
     * Update map position
     * @param latitude
     * @param longitude
     */
    private void updateMap(double latitude, double longitude) {
        mapboxMap.clear();
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(geoView.getText().toString()));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
    }

    private void showStoreForm() {
        geoView.setVisibility(View.GONE);
        fabStore.setVisibility(View.GONE);

        addStoreView.setVisibility(View.VISIBLE);
        addStoreView.setY(homeView.getHeight());
        addStoreView.clearAnimation();
        addStoreView.animate().translationY(0).start();
    }

    @OnClick(R.id.btn_add_store_cancel)
    public void hideStoreForm() {
        geoView.setVisibility(View.VISIBLE);
        fabStore.setVisibility(View.VISIBLE);

        addStoreView.clearAnimation();
        addStoreView.animate().translationY(homeView.getHeight()).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_action:
                showStoreForm();
                break;
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        Log.d("Latlng", point.toString());

        mapboxMap.clear();
        mapboxMap.addMarker(new MarkerOptions().position(point).title("Dropped pin"));
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> locations = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            if (locations.isEmpty()) return;
            geoView.setText(locations.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMyLocationChange(@Nullable Location location) {
//        if (location == null) return;
//        mapboxMap.setCameraPosition(new CameraPosition.Builder()
//                .target(new LatLng(location)).build());
    }

    @Override
    public void onFeatureClick(CarmenFeature feature) {
        Utilities.hideOnScreenKeyboard(this);
        Position position = feature.asPosition();
        updateMap(position.getLatitude(), position.getLongitude());
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
    public ApiService getApiService() {
        return apiService;
    }

    @Override
    public String getStoreName() {
        return null;
    }

    @Override
    public double getStoreLatitude() {
        return 0;
    }

    @Override
    public double getStoreLongitude() {
        return 0;
    }

    @Override
    public boolean getStoreAvailability() {
        return false;
    }

    @Override
    public String getStoreOperationTime() {
        return null;
    }

    @Override
    public String getStoreOwner() {
        return null;
    }

    @Override
    public User getProfile() {
        return null;
    }

    @Override
    public void onAddedStoreSuccess() {

    }

    @Override
    public void onAddedStoreFailure() {

    }
}
