package com.example.college.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.college.adapter.ChatAdapter
import com.example.college.databinding.FragmentChatsBinding
import com.example.college.db.DBKotlinHelper
import com.example.college.db.DatabaseHelper
import com.example.college.db_modules.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var viewHolder: DBKotlinHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendMessage.setOnClickListener {
            val messageBody = binding.messageBody.text.toString().trim()
            if (messageBody.isNotEmpty()) {
                val ref = DatabaseHelper(1)
                val message = Message()
                message.messageText = messageBody
                message.messageUser = FirebaseAuth.getInstance().currentUser?.displayName
                message.messageTime = Date().time
                ref.addMessage(message)
                binding.messageBody.setText("")
            }
        }

        val adapter = ChatAdapter()

        binding.recyclerView.adapter = adapter

        viewHolder.message.observe(viewLifecycleOwner, {
            adapter.addMessage(it)
        })

        viewHolder.getRealTimeUpdate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        viewHolder = ViewModelProvider(this).get(DBKotlinHelper::class.java)
        return binding.root
    }

}