package com.lskycity.androidtools.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 *  main application
 *
 */
public class ToolApplication extends Application {
    public static ToolApplication instance;

    private RequestQueue mRequestQueue;

    public ToolApplication() {
        instance = this;
    }

    public static ToolApplication get() {
        return  instance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue==null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }
}
