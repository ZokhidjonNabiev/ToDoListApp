package com.example.myapplication

interface ToDoListItemClickInterface {
    fun itemDelete(position: Int)
    fun itemEdit(position: Int)
    fun itemIsDone(position: Int, checked: Boolean)
}