package com.grantamos.android.AsyncImageDrawable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Grant on 11/22/13.
 */
public class AsyncImageTask {

    private ImageView mImageView;

    private ImageDecodeRunnable mImageDecodeRunnable;

    private ImageDownloadRunnable mImageDownloadRunnable;

    private Bitmap mBitmap;

    private Drawable mPlaceHolder;

    private String[] mUrls;

    private byte[] mImageBytes;

    private int mCurrentUrl = 0;

    public AsyncImageTask(ImageView imageView, Drawable placeHolder, String... urls){
        mImageView = imageView;
        mUrls = urls;
        mPlaceHolder = placeHolder;

        if(mPlaceHolder != null)
            mImageView.setImageDrawable(mPlaceHolder);
    }

    public byte[] getImageBytes() {
        return mImageBytes;
    }

    public String getImageURL() {
        return mUrls[mCurrentUrl];
    }

    public void onDecodingFinished(Bitmap bitmap) {

        mBitmap = bitmap;

        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(mBitmap);
            }
        });

        mCurrentUrl++;
        if(mCurrentUrl < mUrls.length)
            startDownload();
        else
            mCurrentUrl = mUrls.length - 1;
    }

    private void startDownload() {
        ImageManager.startDownload(this);
    }

    public void onDecodingFailed() {

    }

    public void onDownloadFailed() {

    }

    public void onDownloadFinished(byte[] imageBytes) {
        mImageBytes = imageBytes;
    }
}