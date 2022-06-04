package com.example.iug.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.FragmentAddCourseBinding
import com.example.iug.firebase.models.Course
import com.example.iug.utils.Constants
import com.example.iug.utils.CoursesAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore


class AddCourseFragment : BaseFragment()
{
    private lateinit var binding: FragmentAddCourseBinding
    private lateinit var coursesList: ArrayList<Course>
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var adapter: CoursesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        showProgressDialog()

        binding.recyclerViewCourses.setHasFixedSize(true)
        binding.recyclerViewCourses.layoutManager = LinearLayoutManager(requireActivity())

        fireStore = FirebaseFirestore.getInstance()
        coursesList = arrayListOf()
        adapter = CoursesAdapter(requireActivity() as MainActivity, coursesList, 0)
        binding.recyclerViewCourses.adapter = adapter

        eventChangeListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentAddCourseBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun eventChangeListener()
    {
        fireStore.collection(Constants.COURSES)
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
                        val course = document.document.toObject(Course::class.java)
                        if (document.type == DocumentChange.Type.ADDED)
                                coursesList.add(course)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }
}