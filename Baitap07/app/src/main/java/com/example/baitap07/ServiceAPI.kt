package com.example.baitap07

import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ServiceAPI {
    @Multipart
    @POST("updateimages.php")
    fun updateImage(
        @Part("id") id: RequestBody,
        @Part avatar: MultipartBody.Part
    ): Call<Message>

    companion object {
        private const val BASE_URL = "http://app.iotstar.vn:8081/appfoods/"

        val serviceApi: ServiceAPI by lazy {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ServiceAPI::class.java)
        }
    }
}
