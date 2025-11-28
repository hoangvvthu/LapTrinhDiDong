package com.example.baitap06;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.baitap06.databinding.ActivityViewPagerBinding;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ViewPagerActivity extends AppCompatActivity {

    private ActivityViewPagerBinding binding;
    private List<Images> imagesList;
    private ImagesViewPagerAdapter adapter;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (binding.viewpager.getCurrentItem() == imagesList.size() - 1) {
                binding.viewpager.setCurrentItem(0);
            } else {
                binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem() + 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the Toolbar
        setSupportActionBar(binding.viewPagerToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ViewPager & Indicator Example");
        }

        imagesList = getListImages();
        adapter = new ImagesViewPagerAdapter(imagesList);
        binding.viewpager.setAdapter(adapter);

        // Link ViewPager and Indicator
        binding.circleIndicator.setViewPager(binding.viewpager);

        // Start auto-run
        handler.postDelayed(runnable, 3000);

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
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
}
