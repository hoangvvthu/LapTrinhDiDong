package com.example.baitap02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baitap02.databinding.ActivitySignupBinding
import com.google.android.material.snackbar.Snackbar

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sự kiện click nút mũi tên để tạo tài khoản
        binding.btnArrow.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Snackbar.make(binding.root, "Please fill all fields", Snackbar.LENGTH_SHORT).show()
            } else {
                // TODO: Thêm logic tạo tài khoản thực tế ở đây
                Snackbar.make(binding.root, "Creating account...", Snackbar.LENGTH_SHORT).show()
            }
        }


        binding.tvForgotPassword.setOnClickListener {
            Snackbar.make(binding.root, "Password recovery feature (TODO)", Snackbar.LENGTH_SHORT).show()
        }
    }
}
