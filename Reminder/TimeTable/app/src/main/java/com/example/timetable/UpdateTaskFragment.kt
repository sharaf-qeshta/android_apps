package com.example.timetable

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.timetable.adapter.TasksAdapter
import com.example.timetable.databinding.FragmentUpdateTaskBinding
import com.example.timetable.db_modules.Task
import java.util.*

class UpdateTaskFragment(private val task: Task, private val adapter: TasksAdapter) : DialogFragment()
{
    private lateinit var binding: FragmentUpdateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentUpdateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        val calendar = Calendar.getInstance()
        calendar.time = Task.DF.parse(task.start)
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)


        binding.etFullName.setText(task.title)

        binding.buttonUpdate.setOnClickListener {
            val taskTitle = binding.etFullName.text.toString().trim()
            val taskStart = "$year-$month-$day $hour:$minute:00"

            if (taskTitle.isEmpty()){
                binding.etFullName.error = "the title cannot be empty"
                binding.etFullName.requestFocus()
                return@setOnClickListener
            }

            val oldTitle = task.title
            task.title = taskTitle
            task.start = taskStart
            adapter.update(task, oldTitle)
            dismiss()
        }

        binding.pickDate.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), {
                    _, mYear, mMonth, mDay ->
                year = mYear
                month = mMonth+1
                day = mDay
            }, year, month, day)
            datePicker.show()
        }

        binding.pickTime.setOnClickListener {
            val timePicker = TimePickerDialog(requireContext(), {
                    _, tHour, tMinute ->
                hour = tHour
                minute = tMinute
            }, hour, minute, true)
            timePicker.show()
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        adapter.notifyDataSetChanged()
    }
}