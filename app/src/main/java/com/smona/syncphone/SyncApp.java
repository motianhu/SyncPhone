package com.smona.syncphone;

import android.app.Application;

import com.smona.base.http.HttpManager;

public class SyncApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpManager.init(this);
    }
}
