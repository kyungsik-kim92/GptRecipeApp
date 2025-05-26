package com.example.gptrecipeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gptrecipeapp.WellbeingRecipeModel
import com.example.gptrecipeapp.databinding.ItemWellbeingRecipeBinding

class WellbeingRecipeAdapter :
    ListAdapter<WellbeingRecipeModel, WellbeingRecipeViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WellbeingRecipeViewHolder {
        val binding =
            ItemWellbeingRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WellbeingRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WellbeingRecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<WellbeingRecipeModel>() {
            override fun areItemsTheSame(
                oldItem: WellbeingRecipeModel,
                newItem: WellbeingRecipeModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: WellbeingRecipeModel,
                newItem: WellbeingRecipeModel
            ) = oldItem == newItem
        }

    }
}

class WellbeingRecipeViewHolder(val binding: ItemWellbeingRecipeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WellbeingRecipeModel) {
        binding.wellbeingRecipeModel = item
    }
}