package com.example.iug.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.SemesterItemBinding
import com.example.iug.firebase.models.Semester
import com.example.iug.fragments.CoursesFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SemestersAdapter(private val activity: MainActivity, private var semesters: ArrayList<Semester>)
    : RecyclerView.Adapter<SemestersAdapter.ViewHolder>()
{
    inner class ViewHolder(binding: SemesterItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val order = binding.textViewSemesterOrder
        val start = binding.textViewSemesterStartDate
        val end = binding.textViewSemesterEndDate
        val card = binding.semesterCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            SemesterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val format = SimpleDateFormat("yyyy/MM/dd")
        holder.order.text = "${semesters[position].order}"
        holder.start.text = format.format(Date(semesters[position].startDate*1000))
        holder.end.text = format.format(Date(semesters[position].endDate*1000))

        holder.card.setOnClickListener {
            activity.replaceFragment(CoursesFragment(semesters[position].id))
        }
    }

    override fun getItemCount(): Int
    {
        return semesters.size
    }
}