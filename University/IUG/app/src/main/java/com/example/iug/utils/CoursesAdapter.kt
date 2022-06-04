package com.example.iug.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iug.R
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.CourseItemBinding
import com.example.iug.firebase.Helper
import com.example.iug.firebase.models.Course
import com.example.iug.fragments.UnitsFragment

@Suppress("DEPRECATION")
class CoursesAdapter(private val activity: MainActivity, private var courses: ArrayList<Course>, private val type: Int)
    : RecyclerView.Adapter<CoursesAdapter.ViewHolder>()
{
    inner class ViewHolder(binding: CourseItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val title = binding.textViewCourseTitle
        val description = binding.textViewCourseDescription
        val followUnfollow = binding.buttonFollowUnfollow
        val card = binding.courseCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            CourseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.title.text = courses[position].name
        holder.description.text = courses[position].description
        if (type == 1)
            holder.followUnfollow.text = "Unfollow"

        holder.followUnfollow.setOnClickListener {
            if (holder.followUnfollow.text == "Follow")
            {
                holder.followUnfollow.text = "Unfollow"
                holder.description.visibility = View.VISIBLE
                holder.followUnfollow.setTextColor(activity.resources.getColor(R.color.white))
                Helper().addStudentCourse(courses[position].id)
            }
            else
            {
                holder.followUnfollow.setTextColor(activity.resources.getColor(R.color.white))
                holder.followUnfollow.text = "Follow"
                holder.description.visibility = View.GONE
                Helper().deleteStudentCourse(courses[position].id)
            }
        }

        holder.card.setOnClickListener {
            activity.replaceFragment(UnitsFragment(courses[position].id))
        }
    }

    override fun getItemCount(): Int
    {
        return courses.size
    }
}