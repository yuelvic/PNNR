package org.bitbucket.globehacks.presenters;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.bitbucket.globehacks.models.GeoPoint;
import org.bitbucket.globehacks.models.Store;
import org.bitbucket.globehacks.views.interfaces.HomeView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class HomePresenter extends MvpBasePresenter<HomeView> {

    private static final String TAG = HomePresenter.class.getName();

    private HomeView mView;

    private Subscription storeSubscription;
    private Subscription pointSubscription;
    private Subscription nearbySubscription;

    public void init() {
        mView = getView();
    }

    public void release() {
        checkStoreSubscription();
        checkPointSubscription();
        checkNearbySubscription();
    }

    private void checkStoreSubscription() {
        if (storeSubscription != null && storeSubscription.isUnsubscribed())
            storeSubscription.unsubscribe();
    }

    private void checkPointSubscription() {
        if (pointSubscription != null && pointSubscription.isUnsubscribed())
            pointSubscription.unsubscribe();
    }

    private void checkNearbySubscription() {
        if (nearbySubscription != null && nearbySubscription.isUnsubscribed())
            nearbySubscription.unsubscribe();
    }

    private Store createStore() {
        Store store = new Store();
        store.setName(mView.getStoreName());
        store.setLatitude(mView.getStoreLatitude());
        store.setLongitude(mView.getStoreLongitude());
        store.setAvailability(mView.getStoreAvailability());
        store.setOperationTime(mView.getStoreOperationTime());
        store.setOwner(mView.getStoreOwner());

        return store;
    }

    public void putStore() {
        checkStoreSubscription();

        Store mStore = createStore();
        storeSubscription = mView.getApiService()
                .putStore(mView.getApplicationId(), mView.getRestKey(), mView.getProfile().getToken(), mStore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        store -> {
                            GeoPoint geoPoint = new GeoPoint();
                            geoPoint.setLatitude(mStore.getLatitude());
                            geoPoint.setLongitude(mStore.getLongitude());
                            geoPoint.setMetadata(store);

                            putGeoPoint(geoPoint);
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            mView.onAddedStoreFailure();
                        }
                );
    }

    private void putGeoPoint(GeoPoint geoPoint) {
        Log.d(TAG, mView.getProfile().getObjectId() + " " + mView.getProfile().getToken());

        checkPointSubscription();
        pointSubscription = mView.getApiService()
                .putGeoPoint(mView.getApplicationId(), mView.getRestKey(),
                        mView.getProfile().getToken(), geoPoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        point -> {
                            mView.onAddedStoreSuccess();
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            mView.onAddedStoreFailure();
                        }
                );
    }

    public void getStore(Store store1) {
        checkStoreSubscription();
        storeSubscription = mView.getApiService()
                .getStore(mView.getApplicationId(), mView.getRestKey(), mView.getProfile().getToken(), mView.getProfile().getObjectId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        store -> {
                            mView.onGetStoreSuccess(store1);
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            mView.onGetStoreFailure();
                        }
                );
    }

    public void getNearbyStores() {
        checkNearbySubscription();
        nearbySubscription = mView.getApiService()
                .getGeoPoints(mView.getApplicationId(), mView.getRestKey(),
                        mView.getProfile().getToken(), mView.getMapNWLatitude(),
                        mView.getMapNWLongitude(), mView.getMapSELatitude(),
                        mView.getMapSELongitude(), 20, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        geoPoints -> {
                            mView.onGeoPointLoadSuccess(geoPoints);
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            mView.onGeoPointLoadFailure();
                        }
                );
    }

}
