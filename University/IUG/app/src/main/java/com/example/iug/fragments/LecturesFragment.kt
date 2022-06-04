package com.example.iug.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.FragmentLecturesBinding
import com.example.iug.firebase.models.Lecture
import com.example.iug.utils.Constants
import com.example.iug.utils.LecturesAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class LecturesFragment(private val unitID: String) : BaseFragment()
{
    private lateinit var binding: FragmentLecturesBinding
    private lateinit var lecturesList: ArrayList<Lecture>
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var adapter: LecturesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        showProgressDialog()

        binding.recyclerViewLectures.setHasFixedSize(true)
        binding.recyclerViewLectures.layoutManager = LinearLayoutManager(requireActivity())

        fireStore = FirebaseFirestore.getInstance()
        lecturesList = arrayListOf()
        adapter = LecturesAdapter(requireActivity() as MainActivity, lecturesList)
        binding.recyclerViewLectures.adapter = adapter

        eventChangeListener()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentLecturesBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun eventChangeListener()
    {
        fireStore.collection(Constants.LECTURES).orderBy("order", Query.Direction.ASCENDING)
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
                        val lecture = document.document.toObject(Lecture::class.java)
                        if (lecture.unitID == unitID)
                        {
                            if (document.type == DocumentChange.Type.ADDED)
                                lecturesList.add(lecture)
                            else if (document.type == DocumentChange.Type.REMOVED)
                                lecturesList.removeAt(getIndex(lecture.id))
                            else if (document.type == DocumentChange.Type.MODIFIED)
                                lecturesList[getIndex(lecture.id)] = lecture
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }

    private fun getIndex(id: String): Int
    {
        for (i in 0 until lecturesList.size)
            if (lecturesList[i].id == id)
                return i
        return -1 // to keep compiler happy
    }
}