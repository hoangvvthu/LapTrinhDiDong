package com.example.baitap06;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitap06.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnFragmentExample.setOnClickListener(v -> 
            startActivity(new Intent(MenuActivity.this, MainActivity.class)));

        binding.btnRecyclerExample.setOnClickListener(v -> 
            startActivity(new Intent(MenuActivity.this, RecyclerViewActivity.class)));

        binding.btnFlipperExample.setOnClickListener(v -> 
            startActivity(new Intent(MenuActivity.this, FlipperActivity.class)));

        binding.btnViewPagerExample.setOnClickListener(v -> 
            startActivity(new Intent(MenuActivity.this, ViewPagerActivity.class)));

        binding.btnViewPager2Example.setOnClickListener(v -> 
            startActivity(new Intent(MenuActivity.this, ViewPager2Activity.class)));
    }
}
