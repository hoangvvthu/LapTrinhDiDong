package com.example.baitap02

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baitap02.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Arrow button click
        binding.btnArrow.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (email.isBlank() || pass.isBlank()) {
                Snackbar.make(binding.root, "Please enter email and password", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Logging in...", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Social clicks
        binding.buttonFacebook.setOnClickListener {
            Snackbar.make(binding.root, "Facebook login (TODO)", Snackbar.LENGTH_SHORT).show()
        }
        binding.buttonGoogle.setOnClickListener {
            Snackbar.make(binding.root, "Google login (TODO)", Snackbar.LENGTH_SHORT).show()
        }

        // Go to Sign up screen
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
