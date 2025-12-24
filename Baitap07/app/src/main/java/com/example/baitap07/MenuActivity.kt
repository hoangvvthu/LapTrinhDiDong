package com.example.baitap07

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnBai1: Button = findViewById(R.id.btnBai1)
        val btnBai2: Button = findViewById(R.id.btnBai2)

        // Mở Bài 1: Hồ sơ & Upload ảnh (ProfileActivity)
        btnBai1.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Mở Bài 2: Điều khiển thiết bị (SocketActivity)
        btnBai2.setOnClickListener {
            val intent = Intent(this, SocketActivity::class.java)
            startActivity(intent)
        }
    }
}
