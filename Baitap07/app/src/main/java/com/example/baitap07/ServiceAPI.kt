package com.example.baitap07

import com.google.gson.GsonBuilder
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    fun updateImage(
        @Part("id") id: RequestBody,
        @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<ResponseBody>

    companion object {
        private const val BASE_URL = "http://app.iotstar.vn:8081/appfoods/"

        val serviceApi: ServiceAPI by lazy {
            val gson = GsonBuilder().setLenient().create()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ServiceAPI::class.java)
        }
    }
}
