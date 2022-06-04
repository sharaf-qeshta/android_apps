package com.example.iug.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.LectureItemBinding
import com.example.iug.firebase.models.Lecture
import com.example.iug.fragments.VideoFragment

class LecturesAdapter (private val activity: MainActivity, private var lectures: ArrayList<Lecture>)
    : RecyclerView.Adapter<LecturesAdapter.ViewHolder>()
{
    inner class ViewHolder(binding: LectureItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val title = binding.lectureTitle
        val card = binding.lectureCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LectureItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.title.text = lectures[position].title
        holder.card.setOnClickListener {
            activity.replaceFragment(VideoFragment(lectures[position].link))
        }
    }

    override fun getItemCount(): Int
    {
        return lectures.size
    }
}
