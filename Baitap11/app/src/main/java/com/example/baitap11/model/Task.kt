package com.example.baitap11.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val dueDate: Long? = null // Thời hạn công việc (timestamp)
)
