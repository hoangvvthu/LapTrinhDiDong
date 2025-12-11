package com.example.baitap08;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MyVideosAdapter extends RecyclerView.Adapter<MyVideosAdapter.MyVideoViewHolder> {

    public interface OnVideoClickListener {
        void onVideoClick(int position);
    }

    private final List<VideoShort> videoList;
    private final OnVideoClickListener listener;

    public MyVideosAdapter(List<VideoShort> videoList, OnVideoClickListener listener) {
        this.videoList = videoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. SỬ DỤNG LAYOUT item_my_video.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_video, parent, false);
        return new MyVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVideoViewHolder holder, int position) {
        holder.bind(videoList.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class MyVideoViewHolder extends RecyclerView.ViewHolder {
        // 2. THAM CHIẾU ĐẾN ImageView
        ImageView ivVideoThumbnail;

        public MyVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVideoThumbnail = itemView.findViewById(R.id.ivVideoThumbnail);
        }

        void bind(final VideoShort video, final OnVideoClickListener listener, final int position) {
            // 3. DÙNG GLIDE ĐỂ HIỂN THỊ THUMBNAIL
            if (video.getVideoUrl() != null && !video.getVideoUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(video.getVideoUrl()) // Glide có thể tự lấy thumbnail từ URL video
                        .placeholder(android.R.color.black) // Màu nền trong lúc chờ tải
                        .into(ivVideoThumbnail);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVideoClick(position);
                }
            });
        }
    }
}
