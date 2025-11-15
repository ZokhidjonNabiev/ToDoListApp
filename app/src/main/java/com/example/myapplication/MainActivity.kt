package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.AddNewtaskDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var weekDaysList = mutableListOf<WeekDaysData>(
            WeekDaysData(1, "Mon"),
            WeekDaysData(2, "Tue"),
            WeekDaysData(3, "Wed"),
            WeekDaysData(4, "Thur"),
            WeekDaysData(5, "Fri"),
            WeekDaysData(6, "Sat"),
            WeekDaysData(7, "Sun"),
        )

        val adapter = WeekDaysAdapter(weekDaysList)
        val layoutManager = GridLayoutManager(this, 7, RecyclerView.VERTICAL, false)

        binding.weekdaysRecyclerView.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
        var todoListData = mutableListOf<ToDoListDataType>(
            ToDoListDataType("08.00", "Wake-up", false),
            ToDoListDataType("12.00", "Lunch", false),
            ToDoListDataType("01.00", "Go to Sport", false),
            ToDoListDataType("04.00", "Have a Snack", false),
            ToDoListDataType("06.00", "Dinner", false),
            ToDoListDataType("10.00", "Sleep", false),

        )
        val todoListAdapter = ToDoListAdapter()
        val todoListLayoutManager = LinearLayoutManager(this)

        binding.todoListRecyclerView.apply{
            this.adapter = todoListAdapter
            this.layoutManager = todoListLayoutManager
        }

        binding.swipeToDoList.setOnRefreshListener {
            Handler().postDelayed({
                val refreshedList = todoListData.toMutableList()
                todoListAdapter.submitList(refreshedList)
                todoListData = refreshedList
                binding.swipeToDoList.isRefreshing = false
            }, 5000)

            binding.swipeToDoList.setColorSchemeColors(
                Color.BLUE,
                Color.RED
            )

        }
        binding.addNewTaskBtn.setOnClickListener {

            val addDialogBinding = AddNewtaskDialogBinding.inflate(layoutInflater)

            val addNewTaskDialog: AlertDialog = MaterialAlertDialogBuilder(this)
                .setView(addDialogBinding.root)
                .create()
                addNewTaskDialog.show()
//                .setPositiveButton("Add"){dialog, _ ->
//                    Toast.makeText(this, "You've added a new task", Toast.LENGTH_SHORT).show()
//                }
//                .setNegativeButton("Cancel"){dialog, _->
//                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
//                }
                addDialogBinding.addNewTaskDialogBtn.setOnClickListener {
                    val time = addDialogBinding.inputTime.text.toString().trim()
                    val task = addDialogBinding.inputTask.text.toString().trim()

                    if (time.isNotEmpty() && task.isNotEmpty()){
                        val newList = todoListData.toMutableList()
                        newList.add(todoListData.size, ToDoListDataType(time, task, false))
                        todoListAdapter.submitList(newList)
                        todoListData = newList
                        addNewTaskDialog.dismiss()
                    } else{
                        Toast.makeText(this, "Fill all fields", Toast.LENGTH_LONG).show()
                    }

                }
            addDialogBinding.cancelAddingTaskDialogBtn.setOnClickListener {
                Toast.makeText(this, "You've cancelled", Toast.LENGTH_LONG).show()
                addNewTaskDialog.dismiss()
            }



        }
        todoListAdapter.submitList(todoListData)

        todoListAdapter.setListener(object : ToDoListItemClickInterface{
            override fun itemDelete(position: Int) {
                var newList = todoListData.toMutableList()
                newList.removeAt(position)
                todoListAdapter.submitList(newList)
                todoListData = newList
            }

            override fun itemEdit(position: Int) {
                val dialogBinding = AddNewtaskDialogBinding.inflate(layoutInflater)

                val dialog  = MaterialAlertDialogBuilder(this@MainActivity)
                    .setView(dialogBinding.root)
                    .create()
                    dialog.show()
                val newTime = dialogBinding.inputTime.text.toString().trim()
                val newTask = dialogBinding.inputTask.text.toString().trim()

                dialogBinding.addNewTaskDialogBtn.setOnClickListener {
                    val newTime = dialogBinding.inputTime.text.toString().trim()
                    val newTask = dialogBinding.inputTask.text.toString().trim()

                    if (newTime.isNotEmpty() && newTask.isNotEmpty()){
                        var newList = todoListData.toMutableList()
                        newList[position] = ToDoListDataType(newTime, newTask, false)
                        todoListAdapter.submitList(newList)
                        todoListData = newList
                        dialog.dismiss()
                    }
                }

                dialogBinding.cancelAddingTaskDialogBtn.setOnClickListener {
                    Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
                }
            }

            override fun itemIsDone(position: Int, checked: Boolean) {
                val isChanged = todoListData[position].isDone
                if (isChanged != checked){
                    var newList = todoListData.toMutableList()
                   val updatedItem = newList[position].copy(isDone = checked)
                    newList[position] = updatedItem
                    todoListAdapter.submitList(newList)
                    todoListData = newList
                }
            }
        })

        val itemTouchHelper = ItemTouchHelper(object :MyItemTouchHelper(){
            override fun moveUpDown(from: Int, to: Int) {
                val newList = todoListData.toMutableList()
                val item = newList[from]
                newList[from] = newList[to]
                newList[to] = item

                todoListAdapter.submitList(newList)
                todoListData = newList
            }

            override fun deleteSwipingToLeft(position: Int) {
                var afterDelete = todoListData.toMutableList()
                val deletedItem = afterDelete.removeAt(position)
                todoListAdapter.submitList(afterDelete)
                todoListData = afterDelete

                Snackbar.make(binding.root, "${deletedItem.taskTitle} has been deleted", Snackbar.LENGTH_LONG)
                    .setAction("Recover"){
                        var recoveredList = todoListData.toMutableList()
                        recoveredList.add(position, deletedItem)
                        todoListAdapter.submitList(recoveredList)
                        todoListData = recoveredList

                        Toast.makeText(this@MainActivity, "Task's been successfully recovered",
                            Toast.LENGTH_LONG).show()

                    }
                    .show()
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.todoListRecyclerView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}