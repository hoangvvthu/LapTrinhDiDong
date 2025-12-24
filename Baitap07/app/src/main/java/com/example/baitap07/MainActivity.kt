package com.example.baitap07

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView

    private val startUploadActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(profileImage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bài tập 7"

        profileImage = findViewById(R.id.profile_image)
        val btnLogout: Button = findViewById(R.id.btnLogout)
        
        // Tạo nút để mở Bài tập 2 (Hoặc bạn có thể tận dụng nút Logout nếu muốn nhanh)
        btnLogout.text = "BÀI TẬP 2: SOCKET"
        btnLogout.setOnClickListener {
            val intent = Intent(this, SocketActivity::class.java)
            startActivity(intent)
        }

        profileImage.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startUploadActivity.launch(intent)
        }
    }
}
