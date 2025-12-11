package com.example.baitap08;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); // Giữ cho các liên kết mở trong WebView
        webView.getSettings().setJavaScriptEnabled(true); // Bật JavaScript

        // Tải URL - bạn có thể thay đổi URL này
        webView.loadUrl("https://www.google.com");
    }

    // Cho phép quay lại trang trước trong WebView khi nhấn nút back
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
