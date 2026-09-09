package com.example.newapp.ui.taskList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.newapp.api.Resource
import com.example.newapp.databinding.FragmentTaskListBinding
import java.util.*

class TaskListFragment: Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskListViewModel: TaskListViewModel
    private val taskListAdapter by lazy { TaskListAdapter() }

    private var timer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        taskListViewModel = ViewModelProvider(this)[TaskListViewModel::class.java]

        _binding = FragmentTaskListBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            rvTaskList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = taskListAdapter
            }

            searchTask.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    taskListAdapter.filter.filter(binding.searchTask.text.toString().trim())
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }

        taskListViewModel.getTaskList().observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Success->{
                    result.data?.let {
                        if(it.isNotEmpty()){
                            taskListAdapter.updateTaskList(it)
                        } else {
                            Snackbar.make(
                                binding.root,
                                "No TAsk Found",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        result.errorMessage!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.loadingBar.root.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loadingBar.root.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                taskListViewModel.fetchTaskList()
            }
        }, 0, 1000*30)
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }
}