package com.example.roomdemo;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;

/**
 */

public class BaseApp extends Application{

    private static BaseApp dinoApplication = null;

    public static BaseApp app() {
        return dinoApplication;
    }

    public static BaseApp app(Context context) {
        if (dinoApplication == null) {
            if (context != null) {
                Context application = context.getApplicationContext();
                if (application instanceof BaseApp) {
                    return (BaseApp) application;
                }
            }
            return null;
        }
        return dinoApplication;
    }

    @TargetApi(28)
    @Override
    public void onCreate() {
        super.onCreate();
        dinoApplication = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
