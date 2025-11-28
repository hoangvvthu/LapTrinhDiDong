package com.example.baitap06;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.baitap06.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ViewPager2Adapter viewPager2Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);

        binding.fabAction.setOnClickListener(view -> {
            Toast.makeText(this, "Replace with your own action", Toast.LENGTH_SHORT).show();
        });

        // Set up ViewPager2 Adapter
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager2Adapter = new ViewPager2Adapter(fragmentManager, getLifecycle());
        binding.viewPager2.setAdapter(viewPager2Adapter);

        // Connect TabLayout and ViewPager2 using TabLayoutMediator
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Xác nhận");
                    break;
                case 1:
                    tab.setText("Lấy hàng");
                    break;
                case 2:
                    tab.setText("Đang giao");
                    break;
                case 3:
                    tab.setText("Đánh giá");
                    break;
                case 4:
                    tab.setText("Hủy");
                    break;
            }
        }).attach();

        // Add a listener to handle actions when a tab is selected, like changing the FAB icon
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeFabIcon(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void changeFabIcon(final int index) {
        binding.fabAction.hide();
        // Using Handler(Looper.getMainLooper()) is the modern and safer way
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            switch (index) {
                case 0:
                    binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_baseline_chat_24));
                    break;
                case 1:
                    binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_alt_24));
                    break;
                case 2:
                    binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_baseline_call_24));
                    break;
                default:
                    binding.fabAction.setImageDrawable(getDrawable(android.R.drawable.ic_dialog_email));
                    break;
            }
            binding.fabAction.show();
        }, 200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSearch) {
            Toast.makeText(this, "Bạn đang chọn search", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuNewGroup) {
            Toast.makeText(this, "Bạn đang chọn new group", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuBroadcast) {
            Toast.makeText(this, "Bạn đang chọn broadcast", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuWeb) {
            Toast.makeText(this, "Bạn đang chọn web", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuMessage) {
            Toast.makeText(this, "Bạn đang chọn message", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuSetting) {
            Toast.makeText(this, "Bạn đang chọn setting", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
