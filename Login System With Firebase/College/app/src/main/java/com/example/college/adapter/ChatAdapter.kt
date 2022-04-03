package com.example.college.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.college.db_modules.Message
import com.example.college.databinding.MessageBinding

class ChatAdapter
    : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    var chats = mutableListOf<Message>()

    inner class ViewHolder(binding: MessageBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val sender = binding.sender
        val date = binding.date
        val message = binding.message
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.message.text = chats[position].messageText
        holder.sender.text = chats[position].messageUser
        val date = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
            chats[position].messageTime)
        holder.date.text = date
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun addMessage(message: Message){
        chats.add(message)
        notifyDataSetChanged()
    }
}