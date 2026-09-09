package com.example.newapp.ui.mapContainer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newapp.databinding.LayoutContainerItemBinding
import com.example.newapp.model.Inventory

class ContainerListAdapter: RecyclerView.Adapter<ContainerListAdapter.ContainerListViewHolder>(), Filterable {

    var onContainerClickListener: OnContainerClickListener? = null

    private val inventoryList = mutableListOf<Inventory>()

    inner class ContainerListViewHolder(val binding: LayoutContainerItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerListViewHolder {
        val binding = LayoutContainerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ContainerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerListViewHolder, position: Int) {
        val inventory = inventoryList[position]
        holder.binding.container.text = inventory.contNo

        holder.binding.container.setOnClickListener {
            onContainerClickListener?.let {
                it.onContainerClicked(inventory.contNo)
            }
        }
    }

    override fun getItemCount(): Int {
        return inventoryList.size
    }

    fun updateList(itemList: List<Inventory>){
        inventoryList.clear()
        inventoryList.addAll(itemList)
        differ.submitList(itemList)
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Inventory>() {
        override fun areItemsTheSame(oldItem: Inventory, newItem: Inventory): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Inventory, newItem: Inventory): Boolean {
            return oldItem.contNo == newItem.contNo
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    interface OnContainerClickListener {
        fun onContainerClicked(containerNo: String)
    }

    override fun getFilter(): Filter {
        return containerFilter
    }

    private val containerFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            var filteredList = ArrayList<Inventory>()
            if (p0 == null || p0.isEmpty() ){
                filteredList.addAll(differ.currentList)
            } else {
                val filterPattern: String = p0.toString().lowercase()
                for (item in differ.currentList) {
                    if (item.contNo.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results

        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            p1?.let {
                inventoryList.clear()
                inventoryList.addAll(it.values as List<Inventory>)
                notifyDataSetChanged()
            }
        }
    }
}
