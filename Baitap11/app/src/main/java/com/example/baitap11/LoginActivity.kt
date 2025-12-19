package com.example.baitap11

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.baitap11.databinding.ActivityLoginBinding
import com.example.baitap11.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.RegisterSuccess -> {
                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                }
                is AuthViewModel.AuthState.Success -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("USER_ID", state.user.id)
                        putExtra("USERNAME", state.user.username)
                    }
                    startActivity(intent)
                    finish()
                }
                is AuthViewModel.AuthState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
