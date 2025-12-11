package com.example.baitap08;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements MyVideosAdapter.OnVideoClickListener {

    private ImageView ivProfileImage;
    private TextView tvProfileEmail;
    private RecyclerView rvMyVideos;
    private MyVideosAdapter adapter;
    private final ArrayList<VideoShort> myVideos = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trang cá nhân");

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        rvMyVideos = findViewById(R.id.rvMyVideos);
        FloatingActionButton fabUploadVideo = findViewById(R.id.fabUploadVideo);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Glide.with(this).load(uri).into(ivProfileImage);
                        uploadProfileImageToCloudinary(uri);
                    }
                });

        setupRecyclerView();
        loadUserInfo();

        fabUploadVideo.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            finish(); 
            return true;
        } else if (itemId == R.id.menu_change_avatar) {
            selectImage();
            return true;
        } else if (itemId == R.id.menu_logout) {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyVideos();
    }

    private void setupRecyclerView() {
        adapter = new MyVideosAdapter(myVideos, this);
        rvMyVideos.setLayoutManager(new GridLayoutManager(this, 2));
        rvMyVideos.setAdapter(adapter);
    }

    private void loadUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            tvProfileEmail.setText(user.getEmail());
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(ivProfileImage);
            }
        }
    }

    private void loadMyVideos() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null || user.getEmail() == null) return;

        db.collection("videos")
                .whereEqualTo("userEmail", user.getEmail())
                .orderBy("likes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        myVideos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            VideoShort video = document.toObject(VideoShort.class);
                            myVideos.add(video);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onVideoClick(int position) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("VIDEO_LIST", myVideos);
        intent.putExtra("START_POSITION", position);
        startActivity(intent);
    }

    private void selectImage() {
        imagePickerLauncher.launch("image/*");
    }

    private void uploadProfileImageToCloudinary(Uri imageUri) {
        // <<<--- VIỆC CẦN LÀM: THAY THẾ CHÍNH XÁC TÊN PRESET CỦA BẠN VÀO ĐÂY
        String uploadPreset = "YOUR_UNSIGNED_UPLOAD_PRESET"; // Ví dụ: "ml_default"

        MediaManager.get().upload(imageUri)
                .unsigned(uploadPreset)
                .callback(new UploadCallback() {
                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");
                        updateUserProfile(Uri.parse(imageUrl));
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(ProfileActivity.this, "Lỗi tải ảnh lên: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStart(String requestId) {}
                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}
                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void updateUserProfile(Uri downloadUri) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(downloadUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
