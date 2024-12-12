package org.nebuloss.testapp;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getGlobalContext() {
        return instance.getApplicationContext();
    }
}

