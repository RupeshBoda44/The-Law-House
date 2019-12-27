package com.thelawhouse.Utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
