package com.example.baitap07

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var profileImage: CircleImageView
    private var currentImageUri: Uri? = null

    private val uploadActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.getParcelableExtra<Uri>("imageUri")
            if (imageUri != null) {
                currentImageUri = imageUri
                Glide.with(this).load(currentImageUri).into(profileImage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        profileImage = findViewById(R.id.profile_image)
        profileImage.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            if (currentImageUri != null) {
                intent.putExtra("imageUri", currentImageUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            uploadActivityLauncher.launch(intent)
        }

        val btnLogout: Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}