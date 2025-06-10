package com.example.todolistapp.ui.todo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.data.Task
import com.example.todolistapp.databinding.FragmentTodoBinding
import com.google.android.material.snackbar.Snackbar

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var taskAdapter: TaskAdapter

    private val addTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Task was added or updated through the ViewModel
            // No need to handle the result here as we're observing the LiveData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupFab()
        observeTasks()

        return root
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskChecked = { task ->
                val updatedTask = task.copy(isCompleted = !task.isCompleted)
                todoViewModel.updateTask(updatedTask)
            },
            onTaskDelete = { task ->
                todoViewModel.deleteTask(task)
                Snackbar.make(binding.root, "Task deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        todoViewModel.addTask(task)
                    }
                    .show()
            },
            onTaskEdit = { task ->
                val intent = Intent(context, AddTaskActivity::class.java).apply {
                    putExtra("task_id", task.id)
                }
                addTaskLauncher.launch(intent)
            }
        )

        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }

        // Setup swipe to delete
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = taskAdapter.currentList[position]
                taskAdapter.onTaskDelete(task)
            }
        }).attachToRecyclerView(binding.recyclerViewTasks)
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(context, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }
    }

    private fun observeTasks() {
        todoViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}