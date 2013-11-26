package com.grantamos.android.AsyncImageDrawable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Grant on 11/22/13.
 */
public class ImageDecodeRunnable implements Runnable {

    private final String TAG = "ImageDecodeRunnable";

    private final int MAX_RETRIES = 5;

    private final int RETRY_SLEEP_TIME = 250;

    private AsyncImageTask mAsyncImageTask;

    public ImageDecodeRunnable(AsyncImageTask asyncImageTask){
        mAsyncImageTask = asyncImageTask;
    }

    @Override
    public void run() {

        Bitmap bitmap = null;

        try {
            byte[] imageBytes = mAsyncImageTask.getImageBytes();

            for(int i = 0; i < MAX_RETRIES; i++){
                try{
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                } catch (Throwable t) {
                    Log.e(TAG, "Error decoding image.  Possible out of memory error.");

                    //Try to clear up memory
                    System.gc();

                    if(Thread.interrupted())
                        return;

                    try {
                        Thread.sleep(RETRY_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        } finally {
            if(bitmap == null){
                Log.e(TAG, "Error decoding image. - ImageDecodeRunnable.");

                mAsyncImageTask.onDecodingFailed();
            } else {
                mAsyncImageTask.onDecodingFinished(bitmap);
            }
        }
    }
}
