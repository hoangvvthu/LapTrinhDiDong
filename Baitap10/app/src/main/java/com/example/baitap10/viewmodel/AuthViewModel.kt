package com.example.baitap10.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.baitap10.data.AppDatabase
import com.example.baitap10.model.User
import com.example.baitap10.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    
    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun login() {
        val userVal = username.value ?: ""
        val passVal = password.value ?: ""

        if (userVal.isEmpty() || passVal.isEmpty()) {
            _authState.value = AuthState.Error("Vui lòng điền đầy đủ thông tin")
            return
        }

        viewModelScope.launch {
            val user = repository.login(userVal, passVal)
            if (user != null) {
                _authState.value = AuthState.Success(user)
            } else {
                _authState.value = AuthState.Error("Sai tài khoản hoặc mật khẩu")
            }
        }
    }

    fun register() {
        val userVal = username.value ?: ""
        val passVal = password.value ?: ""

        if (userVal.isEmpty() || passVal.isEmpty()) {
            _authState.value = AuthState.Error("Vui lòng điền đầy đủ thông tin")
            return
        }

        viewModelScope.launch {
            val existingUser = repository.getUserByUsername(userVal)
            if (existingUser != null) {
                _authState.value = AuthState.Error("Tài khoản đã tồn tại")
            } else {
                repository.register(User(username = userVal, password = passVal))
                _authState.value = AuthState.RegisterSuccess
            }
        }
    }

    sealed class AuthState {
        object RegisterSuccess : AuthState()
        data class Success(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
