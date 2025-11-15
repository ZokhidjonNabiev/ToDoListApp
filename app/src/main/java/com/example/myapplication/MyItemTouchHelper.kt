package com.example.myapplication

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class MyItemTouchHelper(): ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.DOWN or ItemTouchHelper.UP,
    ItemTouchHelper.LEFT
){

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.adapterPosition
        val toPos = target.adapterPosition
        moveUpDown(fromPos, toPos)
        return true
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        val pos = viewHolder.adapterPosition
        deleteSwipingToLeft(pos)
    }

    abstract fun moveUpDown(from: Int, to: Int)
    abstract fun deleteSwipingToLeft(position: Int)

}