package com.example.iug.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.FragmentRoomBinding
import com.example.iug.firebase.Helper
import com.example.iug.utils.MessagesAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.iug.firebase.models.Message

class RoomFragment(private val teacherID: String) : BaseFragment()
{
    private lateinit var binding: FragmentRoomBinding
    private lateinit var messagesList: ArrayList<Message>
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var adapter: MessagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSend.setOnClickListener {
            sendMessage()
        }

        binding.recyclerViewChat.setHasFixedSize(true)
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(requireActivity())

        fireStore = FirebaseFirestore.getInstance()
        messagesList = arrayListOf()
        adapter = MessagesAdapter(requireActivity() as MainActivity, messagesList)
        binding.recyclerViewChat.adapter = adapter

        eventChangeListener()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentRoomBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun sendMessage()
    {
        val content = binding.editTextMessageContent.text.toString().trim()
        if (content.isEmpty())
            return
        Helper().sendMessage(teacherID, content)
        binding.editTextMessageContent.setText("")
    }

    private fun eventChangeListener()
    {
        fireStore.collection("${Helper().getCurrentUserId()}$teacherID")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null)
                {
                    error.message?.let { Log.e("sharaf", it) }
                    return@addSnapshotListener
                }

                if (value != null)
                {
                    for (document in value.documentChanges)
                    {
                        val message = document.document.toObject(Message::class.java)
                        if (document.type == DocumentChange.Type.ADDED)
                            messagesList.add(message)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }
}