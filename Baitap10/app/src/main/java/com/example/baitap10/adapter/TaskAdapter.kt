package com.example.baitap10.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baitap10.databinding.ItemTaskBinding
import com.example.baitap10.model.Task

class TaskAdapter(
    private val onTaskDelete: (Task) -> Unit,
    private val onTaskEdit: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.task = task
            
            // Tắt listener cũ để tránh loop
            binding.cbCompleted.setOnCheckedChangeListener(null)
            binding.cbCompleted.isChecked = false // Luôn để false vì hễ tích vào là xóa
            
            binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onTaskDelete(task)
                }
            }
            
            binding.btnEdit.setOnClickListener {
                onTaskEdit(task)
            }
            
            binding.executePendingBindings()
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    }
}
