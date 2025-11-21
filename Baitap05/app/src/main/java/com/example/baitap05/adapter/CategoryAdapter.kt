package com.example.baitap05.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baitap05.R
import com.example.baitap05.model.Category

class CategoryAdapter(private val context: Context, private val categoryList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categoryList[position]
        holder.tenSp.text = category.name
        Glide.with(context).load(category.images).into(holder.images)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Bạn đã chọn category " + category.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val images: ImageView = itemView.findViewById(R.id.image_cate)
        val tenSp: TextView = itemView.findViewById(R.id.tvNameCategory)
    }
}
