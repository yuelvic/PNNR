package org.bitbucket.globehacks.views.fragments;

import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.v5.MapboxNavigation;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.commons.models.Position;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.bitbucket.globehacks.GlobeHack;
import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.models.Store;
import org.bitbucket.globehacks.models.GeoPoint;
import org.bitbucket.globehacks.models.User;
import org.bitbucket.globehacks.presenters.HomePresenter;
import org.bitbucket.globehacks.services.ApiService;
import org.bitbucket.globehacks.utils.Keys;
import org.bitbucket.globehacks.utils.Utilities;
import org.bitbucket.globehacks.views.interfaces.HomeView;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */


public class HomeFragment extends MvpFragment<HomeView, HomePresenter> implements HomeView, MapboxMap.OnMapLongClickListener, MapboxMap.OnMyLocationChangeListener, GeocoderAutoCompleteView.OnFeatureListener, View.OnClickListener, MapboxMap.OnCameraIdleListener,MapboxMap.OnMarkerClickListener {


    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final int WILL_START_RENDERING_MAP = 11;
    private static final int DID_FINISH_RENDERING_MAP = 12;

    private static final int DID_FINISH_LOADING_MAP = 6;

    private SweetAlertDialog loadingDialog;

    @BindView(R.id.view_home) View homeView;
    @BindView(R.id.map_view) MapView mapView;
    @BindView(R.id.geo_view) GeocoderAutoCompleteView geoView;
    @BindView(R.id.fab_action) FloatingActionButton fabStore;
    @BindView(R.id.fab_cancel) FloatingActionButton fabCancel;

    @BindView(R.id.view_add_store) View addStoreView;
    @BindView(R.id.edt_store_name) EditText etStoreName;

    @Inject ApiService apiService;

    // MapBox components
    private CameraPosition cameraPosition;
    private MapboxMap mapboxMap;
    private MapboxNavigation navigation;

    // Flag components
    private boolean pinStore = false;
    private LatLng pinLatLng;

    // Marker components
    private LatLngBounds latLngBounds;

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
        fabCancel.setOnClickListener(this);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            this.mapboxMap.setOnMarkerClickListener(this);
            this.mapboxMap.setOnMapLongClickListener(this);
            this.mapboxMap.setOnMyLocationChangeListener(this);
            this.mapboxMap.setOnCameraIdleListener(this);
            if (mapboxMap.getMyLocation() == null) return;

            LatLng latLng = new LatLng(mapboxMap.getMyLocation());
            cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .build();

            mapboxMap.setCameraPosition(cameraPosition);


        });

        mapView.addOnMapChangedListener(change -> {
            switch (change){
                case WILL_START_RENDERING_MAP:
                    showProgressDialog();
                    break;
                case DID_FINISH_LOADING_MAP:
                    hideProgressDialog();
                    break;
                case DID_FINISH_RENDERING_MAP:
                    hideProgressDialog();
                    break;
            }
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

        LatLng latLng = new LatLng(latitude, longitude);
        mapboxMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(geoView.getText().toString()));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);

        if (pinStore) this.pinLatLng = latLng;
    }

    private void showStoreForm() {
        mapboxMap.clear();

        geoView.setVisibility(View.GONE);
        fabStore.setVisibility(View.GONE);

        addStoreView.setVisibility(View.VISIBLE);
        addStoreView.setY(homeView.getHeight());
        addStoreView.clearAnimation();
        addStoreView.animate().translationY(0).start();
    }

    private void resetStoreForm() {
        pinStore = false;
        etStoreName.setText("");
        fabStore.setImageResource(R.drawable.ic_add);
        fabCancel.setVisibility(View.GONE);

        mapboxMap.clear();
    }

    @OnClick(R.id.btn_add_store_locate)
    public void pinStoreLocation() {
        if (etStoreName.getText().toString().equals(""))
            return;

        Utilities.hideOnScreenKeyboard(this);
        fabStore.setImageResource(R.drawable.ic_done);
        fabCancel.setVisibility(View.VISIBLE);
        pinStore = true;

        hideStoreForm();
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
                if (!pinStore) showStoreForm();
                else{
                    presenter.putStore();
                    showProgressDialog();
                }
                break;
            case R.id.fab_cancel:
                resetStoreForm();
                break;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        return true;
    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        this.pinLatLng = point;

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
    public void onCameraIdle() {
        if (mapboxMap == null) return;
        latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(36.532128, -93.489121))
                .include(new LatLng(25.837058, -106.646234))
                .build();
        presenter.getNearbyStores();
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
        return etStoreName.getText().toString();
    }

    @Override
    public double getStoreLatitude() {
        return pinLatLng.getLatitude();
    }

    @Override
    public double getStoreLongitude() {
        return pinLatLng.getLongitude();
    }

    @Override
    public boolean getStoreAvailability() {
        return true;
    }

    @Override
    public String getStoreOperationTime() {
        return null;
    }

    @Override
    public String getStoreOwner() {
        return getProfile().getObjectId();
    }

    @Override
    public double getMapNWLatitude() {
        return latLngBounds.getNorthWest().getLatitude();
    }

    @Override
    public double getMapNWLongitude() {
        return latLngBounds.getNorthWest().getLongitude();
    }

    @Override
    public double getMapSELatitude() {
        return latLngBounds.getSouthEast().getLatitude();
    }

    @Override
    public double getMapSELongitude() {
        return latLngBounds.getSouthEast().getLongitude();
    }

    @Override
    public User getProfile() {
        return PreferencesManager.getObject(Keys.USER, User.class);
    }

    @Override
    public void onGetStoreSuccess(Store store) {

    }

    @Override
    public void onGetStoreFailure() {

    }


    @Override
    public void onAddedStoreSuccess() {
        hideProgressDialog();
        mapboxMap.clear();
        resetStoreForm();
    }

    @Override
    public void onAddedStoreFailure() {
        hideProgressDialog();

    }

    @Override
    public void onGeoPointLoadSuccess(List<GeoPoint> geoPoints) {
        mapboxMap.clear();
        for (GeoPoint geoPoint : geoPoints) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))
                    .title(""));
        }
    }

    @Override
    public void onGeoPointLoadFailure() {

    }

    private void showProgressDialog(){
        loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loadingDialog.setTitleText("Loading");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void hideProgressDialog(){
        loadingDialog.dismiss();
    }

}
