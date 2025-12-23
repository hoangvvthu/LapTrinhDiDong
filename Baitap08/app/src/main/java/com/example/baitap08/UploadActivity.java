package com.example.baitap08;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private VideoView videoViewPreview;
    private Button btnSelectVideo, btnUploadVideo;
    private ProgressBar progressBarUpload;
    private Uri videoUri;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private ActivityResultLauncher<String> videoPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        videoViewPreview = findViewById(R.id.videoViewPreview);
        btnSelectVideo = findViewById(R.id.btnSelectVideo);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        progressBarUpload = findViewById(R.id.progressBarUpload);

        videoPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        videoUri = uri;
                        videoViewPreview.setVideoURI(videoUri);
                        videoViewPreview.start();
                        btnUploadVideo.setEnabled(true);
                    }
                });

        btnSelectVideo.setOnClickListener(v -> selectVideo());
        btnUploadVideo.setOnClickListener(v -> uploadVideoToCloudinary());

        btnUploadVideo.setEnabled(false);
    }

    private void selectVideo() {
        videoPickerLauncher.launch("video/*");
    }

    private void uploadVideoToCloudinary() {
        if (videoUri == null) return;

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để đăng video", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarUpload.setVisibility(View.VISIBLE);
        btnUploadVideo.setEnabled(false);

        String uploadPreset = "android_preset"; 

        MediaManager.get().upload(videoUri)
                .unsigned(uploadPreset)
                .option("resource_type", "video")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        progressBarUpload.setProgress(0);
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        double progress = (double) bytes / totalBytes;
                        progressBarUpload.setProgress((int) (progress * 100));
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String videoUrl = (String) resultData.get("secure_url");
                        saveVideoInfoToFirestore(videoUrl, currentUser);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(UploadActivity.this, "Lỗi tải lên: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        resetUploadUI();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Do nothing
                    }
                }).dispatch();
    }

    private void saveVideoInfoToFirestore(String videoUrl, FirebaseUser user) {
        String userEmail = user.getEmail();
        String avatarUrl = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : "";

        VideoShort newVideo = new VideoShort(videoUrl, userEmail, avatarUrl, 0, 0);

        db.collection("videos").add(newVideo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Đăng video thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi lưu thông tin video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    resetUploadUI();
                });
    }

    private void resetUploadUI() {
        progressBarUpload.setVisibility(View.GONE);
        btnUploadVideo.setEnabled(true);
    }
}
