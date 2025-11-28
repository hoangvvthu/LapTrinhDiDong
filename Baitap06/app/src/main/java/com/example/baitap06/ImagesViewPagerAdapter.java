package com.example.baitap06;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImagesViewPagerAdapter extends PagerAdapter {

    private List<Images> imagesList;

    public ImagesViewPagerAdapter(List<Images> imagesList) {
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_images, container, false);
        ImageView imgView = view.findViewById(R.id.imgView);

        Images images = imagesList.get(position);
        // Load image from URL using Glide
        Glide.with(container.getContext()).load(images.getImageUrl()).into(imgView);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (imagesList != null) {
            return imagesList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
