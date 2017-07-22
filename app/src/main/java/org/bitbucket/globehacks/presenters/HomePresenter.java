package org.bitbucket.globehacks.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.bitbucket.globehacks.models.Store;
import org.bitbucket.globehacks.views.interfaces.HomeView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Emmanuel Victor Garcia on 19/07/2017.
 */

public class HomePresenter extends MvpBasePresenter<HomeView> {

    private static final String TAG = HomePresenter.class.getSimpleName();

    private HomeView mView;

    private Subscription storeSubscription;

    public void init() {
        mView = getView();
    }

    public void release() {
        checkStoreSubscription();
    }

    private void checkStoreSubscription() {
        if (storeSubscription != null && storeSubscription.isUnsubscribed())
            storeSubscription.unsubscribe();
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
        storeSubscription = mView.getApiService()
                .putStore(mView.getApplicationId(), mView.getRestKey(), mView.getProfile().getToken(), createStore())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        store -> {
                            mView.onAddedStoreSuccess();
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            mView.onAddedStoreFailure();
                        }
                );
    }

}
