package com.example.iug.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.FragmentHomeBinding
import com.example.iug.firebase.Helper
import com.example.iug.firebase.models.Course
import com.example.iug.firebase.relations.StudentCourse
import com.example.iug.utils.Constants
import com.example.iug.utils.CoursesAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : BaseFragment()
{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var coursesList: ArrayList<Course>
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var adapter: CoursesAdapter
    private lateinit var coursesID: ArrayList<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewFollowedCourses.setHasFixedSize(true)
        binding.recyclerViewFollowedCourses.layoutManager = LinearLayoutManager(requireActivity())

        fireStore = FirebaseFirestore.getInstance()
        coursesList = arrayListOf()
        adapter = CoursesAdapter(requireActivity() as MainActivity, coursesList, 1)
        binding.recyclerViewFollowedCourses.adapter = adapter
        coursesID = arrayListOf()

        eventChangeListener1()
        eventChangeListener2()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun eventChangeListener1()
    {
        fireStore.collection(Constants.STUDENT_COURSES)
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
                        val course = document.document.toObject(StudentCourse::class.java)
                        if (course.studentID == Helper().getCurrentUserId())
                        {
                            if (document.type == DocumentChange.Type.ADDED)
                                coursesID.add(course.courseID)
                            else if (document.type == DocumentChange.Type.REMOVED)
                            {
                                val index = getIndex(course.courseID)
                                if (index != -1)
                                    coursesList.removeAt(index)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    private fun eventChangeListener2()
    {
        showProgressDialog()
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
                        if (coursesID.contains(course.id))
                        {
                            if (document.type == DocumentChange.Type.ADDED)
                                coursesList.add(course)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    private fun getIndex(courseId: String): Int
    {
        for (i in 0 until coursesList.size)
            if (coursesList[i].id == courseId)
                return i
        return -1
    }
}