package com.example.iug.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iug.databinding.FragmentAssignmentsBinding


class AssignmentsFragment : BaseFragment()
{
    private lateinit var binding: FragmentAssignmentsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = "Assignments"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentAssignmentsBinding.inflate(layoutInflater)
        return binding.root
    }
}