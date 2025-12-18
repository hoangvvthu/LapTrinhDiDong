package com.example.baitap07

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UploadActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var chooseFileButton: Button
    private lateinit var uploadImagesButton: Button
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.data != null) {
            selectedImageUri = result.data!!.data
            try {
                Glide.with(this).load(selectedImageUri).into(imageView)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openFileChooser()
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        imageView = findViewById(R.id.image_preview)
        chooseFileButton = findViewById(R.id.btn_choose_file)
        uploadImagesButton = findViewById(R.id.btn_upload_images)

        val imageUri = intent.getParcelableExtra<Uri>("imageUri")
        if (imageUri != null) {
            selectedImageUri = imageUri
            Glide.with(this).load(selectedImageUri).into(imageView)
        }

        chooseFileButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    openFileChooser()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    Toast.makeText(this, "Reading external storage is required to select an image.", Toast.LENGTH_LONG).show()
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

        uploadImagesButton.setOnClickListener {
            uploadImage()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val resultIntent = Intent()
                if (selectedImageUri != null) {
                    resultIntent.putExtra("imageUri", selectedImageUri)
                    setResult(Activity.RESULT_OK, resultIntent)
                } else {
                    setResult(Activity.RESULT_CANCELED)
                }
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun uploadImage() {
        selectedImageUri?.let { uri ->
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val fileBytes = inputStream?.readBytes()
                inputStream?.close()

                if (fileBytes != null) {
                    val partMap = mutableMapOf<String, RequestBody>()
                    partMap["id"] = "3".toRequestBody("text/plain".toMediaTypeOrNull())
                    partMap[Const.MY_USERNAME] = "trung1".toRequestBody("text/plain".toMediaTypeOrNull())

                    val requestFile = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
                    val multipartBody = MultipartBody.Part.createFormData(Const.MY_IMAGES, "image.jpg", requestFile)

                    val serviceAPI = ServiceAPI.create()
                    serviceAPI.upload(partMap, multipartBody).enqueue(object : Callback<List<ImageUpload>> {
                        override fun onResponse(call: Call<List<ImageUpload>>, response: Response<List<ImageUpload>>) {
                            if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                                Toast.makeText(applicationContext, "Thành công", Toast.LENGTH_SHORT).show()
                                val resultIntent = Intent()
                                resultIntent.putExtra("imageUri", selectedImageUri)
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, "Thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<ImageUpload>>, t: Throwable) {
                            Toast.makeText(applicationContext, "Gọi API thất bại", Toast.LENGTH_SHORT).show()
                            t.printStackTrace()
                        }
                    })
                } else {
                     Toast.makeText(this, "Failed to read image data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to read image data", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
    }
}