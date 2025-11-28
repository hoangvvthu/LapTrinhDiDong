package com.example.baitap06;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.baitap06.databinding.ActivityViewPager2Binding;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Activity extends AppCompatActivity {

    private ActivityViewPager2Binding binding;
    private List<Images> imagesList;
    private ImagesViewPager2Adapter adapter;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (binding.viewpager2.getCurrentItem() == imagesList.size() - 1) {
                binding.viewpager2.setCurrentItem(0);
            } else {
                binding.viewpager2.setCurrentItem(binding.viewpager2.getCurrentItem() + 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPager2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.viewPager2Toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Slide Images with ViewPager2 and Indicator3");
        }

        imagesList = getListImages();
        adapter = new ImagesViewPager2Adapter(imagesList);

        binding.viewpager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        binding.viewpager2.setAdapter(adapter);

        binding.circleIndicator3.setViewPager(binding.viewpager2);

        binding.viewpager2.setPageTransformer(new DepthPageTransformer());

        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }
        });
    }

    private List<Images> getListImages() {
        List<Images> list = new ArrayList<>();
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/quangcao.PNG"));
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/coffee.jpg"));
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/companypizza.jpeg"));
        list.add(new Images("http://app.iotstar.vn:8081/appfoods/flipper/themoingon.jpeg"));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(@NonNull View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { 
                view.setAlpha(0f);

            } else if (position <= 0) { 
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { 
                view.setAlpha(1 - position);

                view.setTranslationX(pageWidth * -position);
                view.setTranslationZ(-1f);

                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { 
                view.setAlpha(0f);
            }
        }
    }
}
