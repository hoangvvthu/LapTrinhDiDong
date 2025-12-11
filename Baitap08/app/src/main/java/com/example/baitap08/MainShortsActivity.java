package com.example.baitap08;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainShortsActivity extends AppCompatActivity {

    private ViewPager2 viewPagerShorts;
    private VideoShortsAdapter adapter;
    private final List<VideoShort> videoShorts = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shorts);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        viewPagerShorts = findViewById(R.id.viewPagerShorts);
        ImageView ivCurrentUserAvatar = findViewById(R.id.ivCurrentUserAvatar);

        adapter = new VideoShortsAdapter(videoShorts);
        viewPagerShorts.setAdapter(adapter);

        loadCurrentUserAvatar(ivCurrentUserAvatar);

        ivCurrentUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        loadVideos();
    }

    private void loadCurrentUserAvatar(ImageView imageView) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_profile)
                    .into(imageView);
        } else if (user != null) {
            // Nếu user không có ảnh, hiển thị icon profile mặc định
            Glide.with(this)
                    .load(R.drawable.ic_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
    }

    private void loadVideos() {
        db.collection("videos").orderBy("likes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        videoShorts.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            VideoShort videoShort = document.toObject(VideoShort.class);
                            videoShorts.add(videoShort);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
