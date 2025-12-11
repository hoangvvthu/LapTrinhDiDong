package com.example.baitap08;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {

    private ViewPager2 viewPagerPlayer;
    private VideoShortsAdapter adapter;
    private ArrayList<VideoShort> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        viewPagerPlayer = findViewById(R.id.viewPagerPlayer);

        // Lấy dữ liệu được gửi từ ProfileActivity
        if (getIntent().hasExtra("VIDEO_LIST")) {
            videoList = (ArrayList<VideoShort>) getIntent().getSerializableExtra("VIDEO_LIST");
        }
        int startPosition = getIntent().getIntExtra("START_POSITION", 0);

        adapter = new VideoShortsAdapter(videoList);
        viewPagerPlayer.setAdapter(adapter);

        // Chuyển đến đúng video mà người dùng đã nhấn vào
        viewPagerPlayer.setCurrentItem(startPosition, false);
    }
}
