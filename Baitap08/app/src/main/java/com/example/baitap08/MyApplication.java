package com.example.baitap08;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo Cloudinary ở đây. 
        // Code trong này sẽ chỉ chạy một lần khi ứng dụng khởi động.
        CloudinaryConfig.init(this);
    }
}
