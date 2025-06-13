package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemRecipeBinding
import com.example.presentation.model.RecipeModel

class RecipeAdapter : ListAdapter<RecipeModel, RecipeViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RecipeModel>() {
            override fun areItemsTheSame(oldItem: RecipeModel, newItem: RecipeModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: RecipeModel, newItem: RecipeModel) =
                oldItem == newItem

        }
    }
}

class RecipeViewHolder(
    val binding: ItemRecipeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RecipeModel) {
        binding.recipeModel = item
    }
}