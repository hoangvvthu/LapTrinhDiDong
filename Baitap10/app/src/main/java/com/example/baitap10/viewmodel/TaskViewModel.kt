package com.example.baitap10.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.example.baitap10.data.AppDatabase
import com.example.baitap10.model.Task
import com.example.baitap10.receiver.AlarmReceiver
import com.example.baitap10.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application, private val userId: Int) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val tasks: LiveData<List<Task>>
    
    val newTaskTitle = MutableLiveData<String>("")
    val newTaskDescription = MutableLiveData<String>("")
    val selectedDueDate = MutableLiveData<Long?>(null)
    
    // Trạng thái đang sửa công việc nào
    val editingTask = MutableLiveData<Task?>(null)

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        tasks = repository.getTasksByUser(userId).asLiveData()
    }

    fun addTask() {
        val title = newTaskTitle.value ?: ""
        val desc = newTaskDescription.value ?: ""
        val dueDate = selectedDueDate.value
        
        if (title.isNotEmpty()) {
            viewModelScope.launch {
                val currentEditing = editingTask.value
                if (currentEditing == null) {
                    // Thêm mới
                    val taskId = repository.insert(Task(userId = userId, title = title, description = desc, dueDate = dueDate))
                    if (dueDate != null) {
                        scheduleNotification(Task(id = taskId.toInt(), userId = userId, title = title, description = desc, dueDate = dueDate))
                    }
                } else {
                    // Cập nhật công việc hiện tại
                    val updatedTask = currentEditing.copy(title = title, description = desc, dueDate = dueDate)
                    repository.update(updatedTask)
                    cancelNotification(currentEditing) // Hủy thông báo cũ
                    if (dueDate != null) {
                        scheduleNotification(updatedTask) // Đặt thông báo mới
                    }
                }
                resetForm()
            }
        }
    }

    fun resetForm() {
        newTaskTitle.value = ""
        newTaskDescription.value = ""
        selectedDueDate.value = null
        editingTask.value = null
    }

    fun startEditing(task: Task) {
        editingTask.value = task
        newTaskTitle.value = task.title
        newTaskDescription.value = task.description
        selectedDueDate.value = task.dueDate
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
            cancelNotification(task)
        }
    }

    private fun scheduleNotification(task: Task) {
        val alarmManager = getApplication<Application>().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(getApplication(), AlarmReceiver::class.java).apply {
            putExtra("TASK_ID", task.id)
            putExtra("TASK_TITLE", task.title)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        task.dueDate?.let {
            if (it > System.currentTimeMillis()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it, pendingIntent)
            }
        }
    }

    private fun cancelNotification(task: Task) {
        val alarmManager = getApplication<Application>().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(getApplication(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    class Factory(private val application: Application, private val userId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(application, userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
