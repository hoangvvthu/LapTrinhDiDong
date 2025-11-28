package com.example.baitap06;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.baitap06.databinding.ActivityFlipperBinding;

import java.util.ArrayList;
import java.util.List;

public class FlipperActivity extends AppCompatActivity {

    private ActivityFlipperBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlipperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.flipperToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Slide Images with ViewFlipper");
        }

        ActionViewFlipper();
    }

    private void ActionViewFlipper() {
        List<String> arrayListFlipper = new ArrayList<>();
        arrayListFlipper.add("http://app.iotstar.vn:8081/appfoods/flipper/quangcao.PNG");
        arrayListFlipper.add("http://app.iotstar.vn:8081/appfoods/flipper/coffee.jpg");
        arrayListFlipper.add("http://app.iotstar.vn:8081/appfoods/flipper/companypizza.jpeg");
        arrayListFlipper.add("http://app.iotstar.vn:8081/appfoods/flipper/themoingon.jpeg");

        for (int i = 0; i < arrayListFlipper.size(); i++) {
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(arrayListFlipper.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.viewFlipperMain.addView(imageView);
        }

        binding.viewFlipperMain.setFlipInterval(3000);
        binding.viewFlipperMain.setAutoStart(true);

        Animation slide_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        binding.viewFlipperMain.setInAnimation(slide_in);
        binding.viewFlipperMain.setOutAnimation(slide_out);
    }
}
