package com.example.timetable

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.adapter.TasksAdapter
import com.example.timetable.databinding.FragmentTasksBinding
import com.example.timetable.db_modules.Task

class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: TasksAdapter

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim)}

    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TasksAdapter(requireContext())
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter

        binding.floatingActionButton.setOnClickListener{
            AddTaskFragment(adapter).show(childFragmentManager, "")
        }

        binding.sortByDate.setOnClickListener {
            sortByDate(adapter.tasks)
            adapter.notifyDataSetChanged()
        }

        binding.sortByTitle.setOnClickListener {
            sortByTitle(adapter.tasks)
            adapter.notifyDataSetChanged()
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTasks)

        binding.floatingSortButton.setOnClickListener {
                onAddButtonClicked()
        }
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
           return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val currentTask = adapter.tasks[position]

            when(direction){
                ItemTouchHelper.RIGHT -> {
                    UpdateTaskFragment(currentTask, adapter).show(childFragmentManager, "")
                }
                ItemTouchHelper.LEFT -> {
                    AlertDialog.Builder(requireContext()).also {
                        it.setTitle("Are You Sure You Want To Delete This Task")
                        it.setPositiveButton("Yes"){_, _ ->
                            adapter.delete(task = currentTask)
                        }
                        it.setOnDismissListener {
                            adapter.notifyDataSetChanged()
                        }
                    }.create().show()
                }
            }
        }

    }

    /** animation */
    private fun onAddButtonClicked()
    {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    /** animation */
    private fun setVisibility(clicked: Boolean){
        if (!clicked){
            binding.sortByDate.visibility = View.VISIBLE
            binding.sortByTitle.visibility = View.VISIBLE
        }else{
            binding.sortByDate.visibility = View.INVISIBLE
            binding.sortByTitle.visibility = View.INVISIBLE
        }
    }

    /** animation */
    private fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.sortByDate.startAnimation(fromBottom)
            binding.sortByTitle.startAnimation(fromBottom)
            binding.floatingSortButton.startAnimation(rotateOpen)
        }else{
            binding.sortByDate.startAnimation(toBottom)
            binding.sortByTitle.startAnimation(toBottom)
            binding.floatingSortButton.startAnimation(rotateClose)
        }
    }

    /** animation */
    private fun setClickable(clicked: Boolean)
    {
        if (!clicked){
            binding.sortByDate.isClickable = true
            binding.sortByTitle.isClickable = true
        }else{
            binding.sortByDate.isClickable = false
            binding.sortByTitle.isClickable = false
        }
    }


    private fun sortByDate(list: MutableList<Task>)
    {
        Thread{
            for (i in 0 until list.size)
            {
                var min = i
                for (j in i+1 until list.size)
                {
                    if (Task.compareDate(list[min].start, list[j].start) > 0)
                        min = j
                }
                val temp = list[i]
                list[i] = list[min]
                list[min] = temp
            }
        }.start()
    }

    private fun sortByTitle(list: MutableList<Task>)
    {
        Thread{
            for (i in 0 until list.size)
            {
                var min = i
                for (j in i+1 until list.size)
                {
                    if (list[min].title > list[j].title)
                        min = j
                }
                val temp = list[i]
                list[i] = list[min]
                list[min] = temp
            }
        }.start()
    }

}