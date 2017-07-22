package org.bitbucket.globehacks.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

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

    public static void hideOnScreenKeyboard(Fragment fragment) {
        try {
            InputMethodManager imm = (InputMethodManager) fragment.getActivity().getSystemService(INPUT_METHOD_SERVICE);
            if (fragment.getActivity().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(fragment.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
