package com.example.iug.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.TeacherItemBinding
import com.example.iug.firebase.models.Teacher
import com.example.iug.fragments.RoomFragment

class TeachersAdapter (private val activity: MainActivity, private var teachers : ArrayList<Teacher>)
    : RecyclerView.Adapter<TeachersAdapter.ViewHolder>()
{
    inner class ViewHolder(binding: TeacherItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val name = binding.teacherName
        val card = binding.teacherCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            TeacherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.name.text = "${teachers[position].firstName} ${teachers[position].lastName}"

        holder.card.setOnClickListener {
            activity.replaceFragment(RoomFragment(teacherID = teachers[position].id))
        }
    }

    override fun getItemCount(): Int
    {
        return teachers.size
    }
}