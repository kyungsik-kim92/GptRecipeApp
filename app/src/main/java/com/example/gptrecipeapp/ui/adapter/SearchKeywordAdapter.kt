package com.example.gptrecipeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gptrecipeapp.databinding.ItemSearchKeywordBinding

class SearchKeywordAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<String, SearchKeywordViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeywordViewHolder {
        val binding =
            ItemSearchKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchKeywordViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: SearchKeywordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem
        }
    }
}

class SearchKeywordViewHolder(
    private val binding: ItemSearchKeywordBinding,
    private val onItemClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(keyword: String) {
        binding.searchKeyword = keyword
        itemView.setOnClickListener {
            onItemClick(keyword)
        }
    }
}