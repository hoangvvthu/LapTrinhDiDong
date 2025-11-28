package com.example.baitap06;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImagesViewPager2Adapter extends RecyclerView.Adapter<ImagesViewPager2Adapter.ImagesViewHolder> {

    private List<Images> imagesList;

    public ImagesViewPager2Adapter(List<Images> imagesList) {
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        Images image = imagesList.get(position);
        if (image == null) {
            return;
        }
        Glide.with(holder.itemView.getContext()).load(image.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (imagesList != null) {
            return imagesList.size();
        }
        return 0;
    }

    public static class ImagesViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }
}
