package com.example.iug.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iug.activities.MainActivity
import com.example.iug.databinding.FragmentUnitsBinding
import com.example.iug.firebase.models.Unit
import com.example.iug.utils.Constants
import com.example.iug.utils.UnitsAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UnitsFragment(private val courseID: String) : BaseFragment()
{
    private lateinit var binding: FragmentUnitsBinding
    private lateinit var unitsList: ArrayList<Unit>
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var adapter: UnitsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        showProgressDialog()

        binding.recyclerViewUnits.setHasFixedSize(true)
        binding.recyclerViewUnits.layoutManager = LinearLayoutManager(requireActivity())

        fireStore = FirebaseFirestore.getInstance()
        unitsList = arrayListOf()
        adapter = UnitsAdapter(requireActivity() as MainActivity, unitsList)
        binding.recyclerViewUnits.adapter = adapter

        eventChangeListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentUnitsBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun eventChangeListener()
    {
        fireStore.collection(Constants.UNITS).orderBy("order", Query.Direction.ASCENDING)
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
                        val unit = document.document.toObject(Unit::class.java)
                        if (unit.courseID == courseID)
                        {
                            if (document.type == DocumentChange.Type.ADDED)
                                unitsList.add(unit)
                            else if (document.type == DocumentChange.Type.REMOVED)
                                unitsList.removeAt(getIndex(unit.id))
                            else if (document.type == DocumentChange.Type.MODIFIED)
                                unitsList[getIndex(unit.id)] = unit
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }

    private fun getIndex(id: String): Int
    {
        for (i in 0 until unitsList.size)
            if (unitsList[i].id == id)
                return i
        return -1 // to keep compiler happy
    }
}