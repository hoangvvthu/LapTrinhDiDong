package com.example.baitap11.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.baitap11.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun register(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}
