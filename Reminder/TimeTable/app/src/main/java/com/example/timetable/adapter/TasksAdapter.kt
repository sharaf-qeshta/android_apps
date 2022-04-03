package com.example.timetable.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.databinding.TaskBinding
import com.example.timetable.db.TaskOpenHelper
import com.example.timetable.db_modules.Task
import java.util.*

class TasksAdapter(context: Context): RecyclerView.Adapter<TasksAdapter.ViewHolder>()
{

    var tasks = mutableListOf<Task>()
    private val db = TaskOpenHelper(context)

    init {
        tasks = db.getAllData()
    }

    inner class ViewHolder(val binding: TaskBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(TaskBinding.inflate(LayoutInflater.from(parent.context),
            parent, false ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.tvTitle.text = tasks[position].title

        var textColor = Color.BLACK
        var text = tasks[position].start

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val currentDate = "$year-$month-$day $hour:$minute:00"
        val comparison = Task.compareDate(currentDate, tasks[position].start)

        if (comparison > 0)
        {
            text += " Overdue"
            textColor = Color.RED
        }
        holder.binding.tvStart.text = text
        holder.binding.tvStart.setTextColor(textColor)
    }

    override fun getItemCount(): Int
    {
        return tasks.size
    }

    fun add(task: Task)
    {
        db.insert(task)
        tasks.add(task)
        notifyDataSetChanged()
    }

    fun update(task: Task, oldTitle: String)
    {
        db.update(task, oldTitle)
        notifyDataSetChanged()
    }

    fun delete(task: Task)
    {
        db.delete(task)
        tasks.remove(task)
        notifyDataSetChanged()
    }
}