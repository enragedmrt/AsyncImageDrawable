package com.grantamos.android.AsyncImageDrawable;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Grant on 11/22/13.
 */
public class ImageDownloadRunnable implements Runnable {

    private static String TAG = "ImageDownloadRunnable";

    private AsyncImageTask mAsyncImageTask;

    public ImageDownloadRunnable(AsyncImageTask asyncImageTask){
        mAsyncImageTask = asyncImageTask;
    }

    @Override
    public void run() {

        byte[] imageBytes = null;
        final String url = mAsyncImageTask.getImageURL();
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
                return;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();

                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];

                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, len);
                    }

                    // and then we can return your byte array.
                    imageBytes = byteBuffer.toByteArray();

                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w(TAG, "Error while retrieving bitmap from " + url, e);

            mAsyncImageTask.onDownloadFailed();
        } finally {
            if (client != null) {
                client.close();
            }

            if(imageBytes != null)
                mAsyncImageTask.onDownloadFinished(imageBytes);
        }
        return;
    }
}
