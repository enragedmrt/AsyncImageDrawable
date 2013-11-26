package com.grantamos.android.AsyncImageDrawable;

import android.support.v4.util.LruCache;

import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Grant on 11/22/13.
 */
public class ImageManager {

    private static final int IMAGE_CACHE_SIZE = 1024 * 1024 * 4;

    private static final int KEEP_ALIVE_TIME = 1;

    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

    private static final int CORE_POOL_SIZE = 8;

    private static final int MAXIMUM_POOL_SIZE = 8;

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static LruCache<URL, byte[]> mImageCache;

    private static BlockingQueue<Runnable> mImageDownloadQueue;

    private static BlockingQueue<Runnable> mImageDecodeQueue;

    private static ThreadPoolExecutor mImageDownloadThreadPool;

    private static ThreadPoolExecutor mImageDecodeThreadPool;

    private static ImageManager sInstance = null;

    static {
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

        sInstance = new ImageManager();
    }

    ImageManager() {
        mImageCache = new LruCache<URL, byte[]>(IMAGE_CACHE_SIZE) {

            @Override
            protected int sizeOf(URL paramURL, byte[] paramArrayOfByte) {
                return paramArrayOfByte.length;
            }
        };

        mImageDownloadQueue = new LinkedBlockingQueue<Runnable>();

        mImageDecodeQueue = new LinkedBlockingQueue<Runnable>();

        mImageDownloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mImageDownloadQueue);

        mImageDecodeThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mImageDecodeQueue);
    }

    public static void startDownload(AsyncImageTask asyncImageTask) {

    }
}
