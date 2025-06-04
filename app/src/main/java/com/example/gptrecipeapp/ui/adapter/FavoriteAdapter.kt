package com.example.gptrecipeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gptrecipeapp.FavoriteModel
import com.example.gptrecipeapp.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onItemClick: (favoriteModel: FavoriteModel) -> Unit
) : ListAdapter<FavoriteModel, FavoriteViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FavoriteModel>() {
            override fun areItemsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: FavoriteModel,
                newItem: FavoriteModel
            ) = oldItem == newItem

        }
    }
}

class FavoriteViewHolder(
    private val binding: ItemFavoriteBinding,
    private val onItemClick: (favoriteModel: FavoriteModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FavoriteModel) {
        binding.favoriteModel = item
        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}