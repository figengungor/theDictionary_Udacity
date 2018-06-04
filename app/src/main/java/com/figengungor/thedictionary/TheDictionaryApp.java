package com.figengungor.thedictionary;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by figengungor on 5/27/2018.
 */

public class TheDictionaryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
