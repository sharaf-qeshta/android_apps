package com.example.iug.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.FragmentSmestersBinding
import com.example.iug.firebase.models.Semester
import com.example.iug.utils.Constants
import com.example.iug.utils.SemestersAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class SmestersFragment : BaseFragment()
{
    private lateinit var binding: FragmentSmestersBinding
    private lateinit var semestersList: ArrayList<Semester>
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var adapter: SemestersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        showProgressDialog()

        binding.recyclerViewSemesters.setHasFixedSize(true)
        binding.recyclerViewSemesters.layoutManager = LinearLayoutManager(requireActivity())

        fireStore = FirebaseFirestore.getInstance()
        semestersList = arrayListOf()
        adapter = SemestersAdapter(requireActivity() as MainActivity, semestersList)
        binding.recyclerViewSemesters.adapter = adapter

        eventChangeListener()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentSmestersBinding.inflate(layoutInflater)
        return binding.root
    }


    private fun eventChangeListener()
    {
        fireStore.collection(Constants.SEMESTERS).orderBy("order", Query.Direction.ASCENDING)
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
                        val semester = document.document.toObject(Semester::class.java)
                        if (document.type == DocumentChange.Type.ADDED)
                            semestersList.add(semester)
                        else if (document.type == DocumentChange.Type.REMOVED)
                            semestersList.removeAt(getIndex(semester.id))
                        else if (document.type == DocumentChange.Type.MODIFIED)
                            semestersList.set(getIndex(semester.id), semester)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }

    private fun getIndex(id: String): Int
    {
        for (i in 0 until semestersList.size)
            if (semestersList[i].id == id)
                return i
        return -1 // to keep compiler happy
    }
}