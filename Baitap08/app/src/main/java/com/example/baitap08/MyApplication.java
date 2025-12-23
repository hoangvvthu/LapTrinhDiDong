package com.example.baitap08;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CloudinaryConfig.init(this);
    }
}
