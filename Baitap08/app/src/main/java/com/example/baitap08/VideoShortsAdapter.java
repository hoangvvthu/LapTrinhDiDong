package com.example.baitap08;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import java.util.List;

public class VideoShortsAdapter extends RecyclerView.Adapter<VideoShortsAdapter.VideoShortViewHolder> {

    private final List<VideoShort> videoShorts;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 1. SỬA LẠI CONSTRUCTOR CHO ĐÚNG
    public VideoShortsAdapter(List<VideoShort> videoShorts) {
        this.videoShorts = videoShorts;
    }

    @NonNull
    @Override
    public VideoShortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_short, parent, false);
        return new VideoShortViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoShortViewHolder holder, int position) {
        holder.bind(videoShorts.get(position));
    }

    @Override
    public int getItemCount() {
        return videoShorts.size();
    }

    class VideoShortViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        TextView tvEmail;
        TextView tvLikes;
        ImageView ivLike;
        ImageView ivAvatar;
        ExoPlayer player;

        public VideoShortViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }

        void bind(VideoShort videoShort) {
            tvEmail.setText(videoShort.getUserEmail());
            tvLikes.setText(String.valueOf(videoShort.getLikes()));

            // Load user avatar
            if (videoShort.getUserAvatarUrl() != null && !videoShort.getUserAvatarUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(videoShort.getUserAvatarUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.ic_profile)
                        .into(ivAvatar);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.ic_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivAvatar);
            }

            // Like button
            ivLike.setColorFilter(Color.WHITE);
            ivLike.setEnabled(true);
            ivLike.setOnClickListener(v -> {
                if (videoShort.getDocumentId() != null) {
                    updateLikes(videoShort);
                }
            });

            // 2. KIỂM TRA KỸ URL TRƯỚC KHI PHÁT
            if (videoShort.getVideoUrl() != null && !videoShort.getVideoUrl().isEmpty()) {
                player = new ExoPlayer.Builder(itemView.getContext()).build();
                playerView.setPlayer(player);
                MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoShort.getVideoUrl()));
                player.setMediaItem(mediaItem);
                player.prepare();
                player.setPlayWhenReady(true);
            }
        }

        private void updateLikes(VideoShort videoShort) {
            ivLike.setEnabled(false);

            db.collection("videos").document(videoShort.getDocumentId())
                    .update("likes", FieldValue.increment(1))
                    .addOnSuccessListener(aVoid -> {
                        int newLikes = Integer.parseInt(tvLikes.getText().toString()) + 1;
                        tvLikes.setText(String.valueOf(newLikes));
                        ivLike.setColorFilter(Color.RED);
                    })
                    .addOnFailureListener(e -> ivLike.setEnabled(true));
        }
    }

    // 3. GIẢI PHÓNG PLAYER MỘT CÁCH AN TOÀN
    @Override
    public void onViewDetachedFromWindow(@NonNull VideoShortViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.player != null) {
            holder.player.release();
            holder.player = null;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoShortViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.player != null) {
            holder.player.play();
        }
    }
}
