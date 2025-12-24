package com.example.baitap07

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView

    private val startUploadActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                // 1. Hiển thị ảnh ngay lập tức
                Glide.with(this).load(imageUri).into(profileImage)
                
                // 2. Lưu đường dẫn ảnh vào SharedPreferences để ghi nhớ
                saveImageUri(imageUri.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Hồ sơ cá nhân"
        toolbar.setNavigationOnClickListener { finish() }

        profileImage = findViewById(R.id.profile_image)
        val btnLogout: Button = findViewById(R.id.btnLogout)

        // 3. Khôi phục ảnh đã lưu khi mở lại ứng dụng
        val savedUri = getSavedImageUri()
        if (savedUri != null) {
            Glide.with(this).load(Uri.parse(savedUri)).into(profileImage)
        }

        profileImage.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startUploadActivity.launch(intent)
        }

        btnLogout.setOnClickListener { finish() }
    }

    // Hàm lưu Uri vào bộ nhớ máy
    private fun saveImageUri(uri: String) {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profile_image_uri", uri)
            apply()
        }
    }

    // Hàm lấy Uri đã lưu từ bộ nhớ máy
    private fun getSavedImageUri(): String? {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("profile_image_uri", null)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
