package com.example.myapplication

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemTodoTaskListLayoutBinding

class MyDiffUtils: DiffUtil.ItemCallback<ToDoListDataType>(){
    override fun areItemsTheSame(
        oldItem: ToDoListDataType,
        newItem: ToDoListDataType
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: ToDoListDataType,
        newItem: ToDoListDataType
    ): Boolean {
        return oldItem == newItem
    }


}
class ToDoListAdapter:
    ListAdapter<ToDoListDataType, ToDoListAdapter.MyViewHolder>(MyDiffUtils()) {
    private var getPosition: ToDoListItemClickInterface? = null

    fun setListener(listener: ToDoListItemClickInterface){
        getPosition = listener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ItemTodoTaskListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val time = getItem(position).time
        val taskTitle = getItem(position).taskTitle
        val done = getItem(position).isDone
        holder.bind(time, taskTitle, done)
    }

    inner class MyViewHolder(val binding: ItemTodoTaskListLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(time: String, taskTitle: String, done: Boolean){
            binding.todoItemTitle.text = taskTitle
            binding.todoItemTime.text = time
            binding.todoItemCheckBox.isChecked = done
            val tv = binding.todoItemTitle
            if (done){
                tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        init {
            binding.todoItemDeleteBtn.setOnClickListener {
                getPosition?.itemDelete(adapterPosition)
            }
            binding.todoItemEditBtn.setOnClickListener {
                getPosition?.itemEdit(adapterPosition)
            }

            binding.todoItemCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                getPosition?.itemIsDone(adapterPosition, isChecked)
            }
        }
    }

}