package com.example.baitap10

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.baitap10.adapter.TaskAdapter
import com.example.baitap10.databinding.ActivityMainBinding
import com.example.baitap10.util.DateUtils
import com.example.baitap10.viewmodel.TaskViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModel.Factory(application, intent.getIntExtra("USER_ID", -1))
    }

    // Đăng ký callback để yêu cầu quyền thông báo
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Bạn cần cấp quyền thông báo để nhận nhắc nhở!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val username = intent.getStringExtra("USERNAME") ?: ""
        binding.tvWelcome.text = "Chào mừng, $username"

        checkNotificationPermission()
        setupRecyclerView()
        setupDatePicker()
        observeViewModel()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.selectedDueDate.observe(this) { timestamp ->
            if (timestamp != null) {
                binding.tvSelectedDate.text = "Nhắc vào: ${DateUtils.formatDate(timestamp)}"
            } else {
                binding.tvSelectedDate.text = "Chưa chọn"
            }
        }
    }

    private fun setupDatePicker() {
        binding.btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            viewModel.selectedDueDate.value?.let { calendar.timeInMillis = it }
            
            DatePickerDialog(this, { _, year, month, day ->
                calendar.set(year, month, day)
                TimePickerDialog(this, { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    
                    viewModel.selectedDueDate.value = calendar.timeInMillis
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setupRecyclerView() {
        val adapter = TaskAdapter(
            onTaskDelete = { task ->
                viewModel.deleteTask(task)
            },
            onTaskEdit = { task ->
                viewModel.startEditing(task)
            }
        )
        binding.rvTasks.adapter = adapter

        viewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }
    }
}
