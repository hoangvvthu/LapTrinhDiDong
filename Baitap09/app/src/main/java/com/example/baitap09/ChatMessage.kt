package com.example.baitap09

data class ChatMessage(
    val message: String,
    val sender: String, // "customer" or "manager"
    val timestamp: Long = System.currentTimeMillis()
)
