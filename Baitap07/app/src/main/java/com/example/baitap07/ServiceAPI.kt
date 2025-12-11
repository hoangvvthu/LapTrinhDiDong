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
import retrofit2.http.PartMap

interface ServiceAPI {

    @Multipart
    @POST("updateimages.php")
    fun upload(
        @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part avatar: MultipartBody.Part
    ): Call<List<ImageUpload>>

    companion object {
        private const val BASE_URL = "http://app.iotstar.vn:8081/appfoods/"

        fun create(): ServiceAPI {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ServiceAPI::class.java)
        }
    }
}