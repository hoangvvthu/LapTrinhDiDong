package com.example.baitap05.api

import com.example.baitap05.model.Category
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("categories.php")
    fun getCategoryAll(): Call<List<Category>>
}
