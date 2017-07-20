package org.bitbucket.globehacks.views.interfaces;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.kinvey.android.Client;

/**
 * Created by Emmanuel Victor Garcia on 7/20/17.
 */

public interface BaseView extends MvpView {

    Client getKinveyClient();

}
