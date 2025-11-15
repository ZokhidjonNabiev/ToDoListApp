package com.example.myapplication

import android.R
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.util.TypedValueCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemWeekdaysLayoutBinding
import androidx.core.graphics.toColorInt

class WeekDaysAdapter(private val weekDaysList: List<WeekDaysData>): RecyclerView.Adapter<WeekDaysAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(ItemWeekdaysLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val number = weekDaysList[position].dateNum
        val title = weekDaysList[position].dateTitle
        val size = if (position == 3) 20f else 15f
        val padding = if (position == 3) 12f else 10f
        val color = if (position == 3) "#DFBD43" else "#4C4117"

        holder.bind(number, title, size,padding, color)
    }

    override fun getItemCount(): Int {
        return weekDaysList.size
    }

    inner class MyViewHolder(val binding: ItemWeekdaysLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dayNum: Int, dayTitle: String, size: Float, padding: Float, color: String){
            val paddingPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                padding.toFloat(),
                binding.root.context.resources.displayMetrics
            ).toInt()

            val backgroundDrawable = binding.weekDayNumber.background
            val wrappedDrawable = DrawableCompat.wrap(backgroundDrawable).mutate()
            DrawableCompat.setTint(wrappedDrawable, color.toColorInt())
            binding.weekDayNumber.text = dayNum.toString()
            binding.weekDayNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

            binding.weekDayNumber.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
            binding.weekDayTitle.text = dayTitle
            binding.weekDayTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        }
    }
}