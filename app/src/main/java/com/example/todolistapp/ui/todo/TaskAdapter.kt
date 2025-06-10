package com.example.todolistapp.ui.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.data.Task
import com.example.todolistapp.databinding.ItemTaskBinding
import com.example.todolistapp.utils.DateUtils

class TaskAdapter(
    private val onTaskChecked: (Task) -> Unit,
    val onTaskDelete: (Task) -> Unit,
    private val onTaskEdit: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(task: Task) {
            binding.apply {
                textTitle.text = task.title
                textDescription.text = task.description
                checkBoxTask.isChecked = task.isCompleted
                
                task.dueDate?.let { date ->
                    textDueDate.text = DateUtils.formatDateTime(date)
                    textDueDate.visibility = ViewGroup.VISIBLE
                } ?: run {
                    textDueDate.visibility = ViewGroup.GONE
                }

                checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked != task.isCompleted) {
                        onTaskChecked(task)
                    }
                }

                buttonEdit.setOnClickListener {
                    onTaskEdit(task)
                }
            }
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
} 