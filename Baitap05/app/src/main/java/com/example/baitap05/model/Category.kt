package com.example.baitap05.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("images")
    val images: String,
    @SerializedName("description")
    val description: String
) : Serializable
