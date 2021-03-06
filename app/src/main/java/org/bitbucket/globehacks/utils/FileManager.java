package org.bitbucket.globehacks.utils;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.shawnlin.preferencesmanager.PreferencesManager;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.bitbucket.globehacks.R;
import org.bitbucket.globehacks.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Emmanuel Victor Garcia on 23/07/2017.
 */

public class FileManager {

    public interface UploadCallback {
        void onSuccess(String url);
        void onFailure();
    }

    public static File getOutputMediaFile() {
        File storageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Pnnr");
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) return null;
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(storageDir.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
    }

    public void uploadImage(Context context, String objectId, File image, final UploadCallback callback) {
        try {
            new MultipartUploadRequest(context, context.getString(R.string.api_base_url) + context.getString(R.string.api_key_application)
                    + "/" + context.getString(R.string.api_key_rest) + "/files/" + objectId + "/avatar.jpg?overwrite=true")
                    .addFileToUpload(image.getAbsolutePath(), "image")
                    .addHeader("Content-Type", "multipart/form-data")
                    .addHeader("user-token", PreferencesManager.getObject(Keys.USER, User.class).getToken())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                            if (callback != null) callback.onFailure();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.d("url", serverResponse.getBodyAsString());
                            if (callback != null) callback.onSuccess(serverResponse.getBodyAsString());
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .setMaxRetries(2)
                    .startUpload();
        } catch (MalformedURLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
