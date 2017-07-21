package org.bitbucket.globehacks.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public class Utilities {

    public static String checkConnectionStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        if (networkInfos != null) {
            for (NetworkInfo networkInfo : networkInfos) {
                if (networkInfo != null && (networkInfo.getState() == NetworkInfo.State.CONNECTED
                        || networkInfo.getState() == NetworkInfo.State.CONNECTING)) {
                    return networkInfo.getTypeName();
                }
            }
        }
        return null;
    }

}
