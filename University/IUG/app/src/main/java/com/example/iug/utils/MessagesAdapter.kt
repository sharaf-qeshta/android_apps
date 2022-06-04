package com.example.iug.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.MessageItemBinding
import com.example.iug.firebase.models.Message

import kotlin.collections.ArrayList

class MessagesAdapter (private val activity: MainActivity, private var messages: ArrayList<Message>)
    : RecyclerView.Adapter<MessagesAdapter.ViewHolder>()
{
    inner class ViewHolder(binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val messageContent = binding.textViewMessageContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            MessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.messageContent.text = messages[position].content
    }

    override fun getItemCount(): Int
    {
        return messages.size
    }
}