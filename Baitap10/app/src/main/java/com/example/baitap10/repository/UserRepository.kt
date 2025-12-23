package com.example.baitap10.repository

import com.example.baitap10.data.UserDao
import com.example.baitap10.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun register(user: User) = userDao.register(user)
    suspend fun login(username: String, password: String) = userDao.login(username, password)
    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
}
