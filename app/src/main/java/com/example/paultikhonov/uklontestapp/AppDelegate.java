package com.example.paultikhonov.uklontestapp;

import android.app.Application;

import com.example.paultikhonov.uklontestapp.utils.DatabaseInitializer;

/**
 * Created by PaulTikhonov on 17.01.2018.
 */

public class AppDelegate extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseInitializer.populateAsync(AppDatabase.getAppDatabase(this));
    }
}
