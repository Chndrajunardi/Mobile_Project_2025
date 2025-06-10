package com.example.todolistapp.ui.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistapp.data.Task
import com.example.todolistapp.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var selectedDate: Calendar? = null
    private var taskId: Int = -1
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDateTimePicker()
        setupSaveButton()
        setupEditMode()
    }

    private fun setupEditMode() {
        taskId = intent.getIntExtra("task_id", -1)
        if (taskId != -1) {
            viewModel.getTaskById(taskId) { task ->
                task?.let {
                    binding.titleEditText.setText(it.title)
                    binding.descriptionEditText.setText(it.description)
                    if (it.dueDate != null) {
                        selectedDate = it.dueDate
                        binding.dueDateButton.text = dateFormat.format(it.dueDate.time)
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupDateTimePicker() {
        binding.dueDateButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                showTimePicker(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(calendar: Calendar) {
        TimePickerDialog(
            this,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                selectedDate = calendar
                binding.dueDateButton.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if (title.isBlank()) {
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(
                id = if (taskId != -1) taskId else 0,
                title = title,
                description = description,
                dueDate = selectedDate,
                isCompleted = false
            )

            if (taskId != -1) {
                viewModel.updateTask(task)
            } else {
                viewModel.addTask(task)
            }
            
            setResult(RESULT_OK)
            finish()
        }
    }
} 