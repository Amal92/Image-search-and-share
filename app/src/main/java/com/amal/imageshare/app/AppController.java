package com.amal.imageshare.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.amal.imageshare.Utils.Const;
import com.amal.imageshare.Utils.LruBitmapCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.ArrayList;



/**
 * Created by Amal on 28-06-2015.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
    public static File cachePath;

    private static AppController mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try {
            cachePath = new File(getCacheDir(), Const.CACHED_DIRECTORY_NAME);
            cachePath.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
