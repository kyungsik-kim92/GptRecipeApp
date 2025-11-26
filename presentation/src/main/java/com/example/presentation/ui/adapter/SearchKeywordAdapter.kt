package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemSearchKeywordBinding
import com.example.presentation.model.SearchHistoryModel

class SearchKeywordAdapter(
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<SearchHistoryModel, SearchKeywordViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeywordViewHolder {
        val binding =
            ItemSearchKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchKeywordViewHolder(binding, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: SearchKeywordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchHistoryModel>() {
            override fun areItemsTheSame(oldItem: SearchHistoryModel, newItem: SearchHistoryModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SearchHistoryModel,
                newItem: SearchHistoryModel
            ) =
                oldItem == newItem
        }
    }
}

class SearchKeywordViewHolder(
    private val binding: ItemSearchKeywordBinding,
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SearchHistoryModel) {
        binding.searchKeyword = item.keyword
        itemView.setOnClickListener {
            onItemClick(item.keyword)
        }
        binding.btnDelete.setOnClickListener {
            onDeleteClick(item.keyword)
        }
    }
}