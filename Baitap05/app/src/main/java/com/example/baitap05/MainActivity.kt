package com.example.baitap05

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baitap05.adapter.CategoryAdapter
import com.example.baitap05.api.APIService
import com.example.baitap05.api.RetrofitClient
import com.example.baitap05.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rcCate: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var apiService: APIService
    private var categoryList: List<Category> = ArrayList()
    private val BASE_URL = "http://app.iotstar.vn:8081/appfoods/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        anhXa()
        getCategory()
    }

    private fun anhXa() {
        rcCate = findViewById(R.id.rc_category)
    }

    private fun getCategory() {
        apiService = RetrofitClient.getRetrofit(BASE_URL).create(APIService::class.java)
        apiService.getCategoryAll().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful && response.body() != null) {
                    categoryList = response.body()!!
                    categoryAdapter = CategoryAdapter(this@MainActivity, categoryList)
                    rcCate.setHasFixedSize(true)
                    val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                    rcCate.layoutManager = layoutManager
                    rcCate.adapter = categoryAdapter
                    categoryAdapter.notifyDataSetChanged()
                } else {
                    Log.e("MainActivity", "Response not successful or body is null. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("MainActivity", "API call failed", t)
            }
        })
    }
}
