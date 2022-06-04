package com.example.iug.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.UnitItemBinding
import com.example.iug.firebase.models.Unit
import com.example.iug.fragments.LecturesFragment

class UnitsAdapter(private val activity: MainActivity, private var units : ArrayList<Unit>)
    : RecyclerView.Adapter<UnitsAdapter.ViewHolder>()
{
    inner class ViewHolder(binding: UnitItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val title = binding.unitTitle
        val card = binding.unitCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            UnitItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.title.text = units[position].title
        holder.card.setOnClickListener {
            activity.replaceFragment(LecturesFragment(unitID = units[position].id))
        }
    }

    override fun getItemCount(): Int
    {
        return units.size
    }
}