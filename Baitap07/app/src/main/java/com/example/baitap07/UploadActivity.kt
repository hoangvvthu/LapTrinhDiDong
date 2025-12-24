package com.example.baitap07

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadActivity : AppCompatActivity() {

    private lateinit var btnChoose: Button
    private lateinit var btnUpload: Button
    private lateinit var imageViewChoose: ImageView
    private var mUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val toolbar: Toolbar = findViewById(R.id.toolbarUpload)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Back"
        toolbar.setNavigationOnClickListener { finish() }

        btnChoose = findViewById(R.id.btnChoose)
        btnUpload = findViewById(R.id.btnUpload)
        imageViewChoose = findViewById(R.id.imgChoose)

        btnChoose.setOnClickListener { checkPermissionAndOpenGallery() }
        
        btnUpload.setOnClickListener {
            if (mUri != null) {
                uploadImage()
            } else {
                Toast.makeText(this, "Vui lòng chọn ảnh trước!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) checkPermissionAndOpenGallery()
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            mUri = result.data!!.data
            Glide.with(this).load(mUri).into(imageViewChoose)
        }
    }

    private fun uploadImage() {
        val progress = ProgressDialog(this).apply { setMessage("Đang tải lên..."); show() }
        try {
            val inputStream = contentResolver.openInputStream(mUri!!)
            val fileBytes = inputStream?.readBytes() ?: return
            inputStream.close()

            val idRequestBody = "3".toRequestBody("text/plain".toMediaTypeOrNull())
            val requestFile = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
            
            val partMap = HashMap<String, RequestBody>()
            partMap["image\"; filename=\"avatar.jpg\""] = requestFile

            ServiceAPI.serviceApi.updateImage(idRequestBody, partMap).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    progress.dismiss()
                    if (response.isSuccessful) {
                        Toast.makeText(this@UploadActivity, "Upload thành công!", Toast.LENGTH_SHORT).show()
                        val resultIntent = Intent()
                        resultIntent.data = mUri
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(this@UploadActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) { progress.dismiss() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
