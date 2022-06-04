package com.example.iug.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.FragmentChatBinding
import com.example.iug.firebase.models.Teacher
import com.example.iug.utils.Constants
import com.example.iug.utils.TeachersAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class ChatFragment : BaseFragment()
{
    private lateinit var binding: FragmentChatBinding
    private lateinit var teachersList: ArrayList<Teacher>
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var adapter: TeachersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        showProgressDialog()

        binding.recyclerViewTeachers.setHasFixedSize(true)
        binding.recyclerViewTeachers.layoutManager = LinearLayoutManager(requireActivity())

        fireStore = FirebaseFirestore.getInstance()
        teachersList = arrayListOf()
        adapter = TeachersAdapter(requireActivity() as MainActivity, teachersList)
        binding.recyclerViewTeachers.adapter = adapter

        eventChangeListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun eventChangeListener()
    {
        fireStore.collection(Constants.TEACHERS)
            .addSnapshotListener { value, error ->
                if (error != null)
                {
                    dismissProgressDialog()
                    error.message?.let { Log.e("sharaf", it) }
                    return@addSnapshotListener
                }

                if (value != null)
                {
                    for (document in value.documentChanges)
                    {
                        dismissProgressDialog()
                        val teacher = document.document.toObject(Teacher::class.java)
                        if (document.type == DocumentChange.Type.ADDED)
                             teachersList.add(teacher)
                        else if (document.type == DocumentChange.Type.MODIFIED)
                            teachersList[getIndex(teacher.id)] = teacher
                        else if (document.type == DocumentChange.Type.REMOVED)
                            teachersList.removeAt(getIndex(teacher.id))
                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }


    private fun getIndex(id: String): Int
    {
        for (i in 0 until teachersList.size)
            if (teachersList[i].id == id)
                return i
        return -1
    }
}