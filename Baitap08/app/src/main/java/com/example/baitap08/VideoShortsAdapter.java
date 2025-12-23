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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import java.util.List;

public class VideoShortsAdapter extends RecyclerView.Adapter<VideoShortsAdapter.VideoShortViewHolder> {

    private final List<VideoShort> videoShorts;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        TextView tvEmail, tvLikes, tvDislikes;
        ImageView ivLike, ivDislike, ivAvatar;
        ExoPlayer player;

        public VideoShortViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvDislikes = itemView.findViewById(R.id.tvDislikes);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivDislike = itemView.findViewById(R.id.ivDislike);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }

        void bind(VideoShort videoShort) {
            tvEmail.setText(videoShort.getUserEmail());
            tvLikes.setText(String.valueOf(videoShort.getLikes()));
            tvDislikes.setText(String.valueOf(videoShort.getDislikes()));

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

            // UI trạng thái Like/Dislike dựa trên danh sách trong database
            boolean isLiked = videoShort.getLikedBy().contains(currentUserId);
            boolean isDisliked = videoShort.getDislikedBy().contains(currentUserId);
            
            ivLike.setColorFilter(isLiked ? Color.RED : Color.WHITE);
            ivDislike.setColorFilter(isDisliked ? Color.parseColor("#808080") : Color.WHITE);

            ivLike.setOnClickListener(v -> toggleLike(videoShort));
            ivDislike.setOnClickListener(v -> toggleDislike(videoShort));

            if (videoShort.getVideoUrl() != null && !videoShort.getVideoUrl().isEmpty()) {
                try {
                    player = new ExoPlayer.Builder(itemView.getContext()).build();
                    playerView.setPlayer(player);
                    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoShort.getVideoUrl()));
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.setPlayWhenReady(true);
                } catch (Exception e) {}
            }
        }

        private void toggleLike(VideoShort videoShort) {
            String docId = videoShort.getDocumentId();
            boolean isCurrentlyLiked = videoShort.getLikedBy().contains(currentUserId);
            boolean isCurrentlyDisliked = videoShort.getDislikedBy().contains(currentUserId);

            if (isCurrentlyLiked) {
                // Hủy Like
                db.collection("videos").document(docId).update(
                        "likes", FieldValue.increment(-1),
                        "likedBy", FieldValue.arrayRemove(currentUserId)
                );
                videoShort.getLikedBy().remove(currentUserId);
                videoShort.setLikes(videoShort.getLikes() - 1);
            } else {
                // Thêm Like
                if (isCurrentlyDisliked) toggleDislike(videoShort); // Nếu đang dislike thì hủy nó đi
                db.collection("videos").document(docId).update(
                        "likes", FieldValue.increment(1),
                        "likedBy", FieldValue.arrayUnion(currentUserId)
                );
                videoShort.getLikedBy().add(currentUserId);
                videoShort.setLikes(videoShort.getLikes() + 1);
            }
            notifyItemChanged(getAdapterPosition());
        }

        private void toggleDislike(VideoShort videoShort) {
            String docId = videoShort.getDocumentId();
            boolean isCurrentlyLiked = videoShort.getLikedBy().contains(currentUserId);
            boolean isCurrentlyDisliked = videoShort.getDislikedBy().contains(currentUserId);

            if (isCurrentlyDisliked) {
                // Hủy Dislike
                db.collection("videos").document(docId).update(
                        "dislikes", FieldValue.increment(-1),
                        "dislikedBy", FieldValue.arrayRemove(currentUserId)
                );
                videoShort.getDislikedBy().remove(currentUserId);
                videoShort.setDislikes(videoShort.getDislikes() - 1);
            } else {
                // Thêm Dislike
                if (isCurrentlyLiked) toggleLike(videoShort); // Nếu đang like thì hủy nó đi
                db.collection("videos").document(docId).update(
                        "dislikes", FieldValue.increment(1),
                        "dislikedBy", FieldValue.arrayUnion(currentUserId)
                );
                videoShort.getDislikedBy().add(currentUserId);
                videoShort.setDislikes(videoShort.getDislikes() + 1);
            }
            notifyItemChanged(getAdapterPosition());
        }
    }

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
        if (holder.player != null) holder.player.play();
    }
}
