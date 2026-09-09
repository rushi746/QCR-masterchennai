package com.example.newapp.ui.taskList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newapp.databinding.LayoutItemTaskBinding
import com.example.newapp.model.Task

class TaskListAdapter: RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>(), Filterable {

    private val taskList = mutableListOf<Task>()

    inner class TaskListViewHolder(val binding: LayoutItemTaskBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val binding = LayoutItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TaskListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = taskList[position]
        holder.binding.contNoValue.text = task.containerNo
        holder.binding.vehicleValue.text = task.trailerNo
        holder.binding.locationValue.text = task.contLoc
        holder.binding.tatValue.text = task.contSize
        holder.binding.tvLoadLabel.text = task.taskType
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.containerNo == newItem.containerNo
        }

    }

    fun updateTaskList(tasks: List<Task>){
        taskList.clear()
        taskList.addAll(tasks)
        differ.submitList(tasks)
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredArray = ArrayList<Task>()

            if (p0 == null || p0.isEmpty()){
                filteredArray.addAll(differ.currentList)
            } else {
                val filterPattern = p0.toString().lowercase()
                for (item in differ.currentList){
                    if (item.containerNo.lowercase().contains(filterPattern)
                        || item.trailerNo.lowercase().contains(filterPattern)) {
                        filteredArray.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredArray
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            p1?.let {
                taskList.clear()
                taskList.addAll(it.values as List<Task>)
                notifyDataSetChanged()
            }
        }
    }
}
