package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemIngredientsBinding
import com.example.presentation.model.IngredientsModel


class IngredientsAdapter(
    private val isClickable: Boolean = true,
    private val onItemClick: (String) -> Unit
) : ListAdapter<IngredientsModel, IngredientsViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding =
            ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsViewHolder(binding, isClickable, onItemClick)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<IngredientsModel>() {
            override fun areItemsTheSame(
                oldItem: IngredientsModel,
                newItem: IngredientsModel
            ) = oldItem == newItem


            override fun areContentsTheSame(
                oldItem: IngredientsModel,
                newItem: IngredientsModel
            ) = oldItem == newItem

        }
    }
}

class IngredientsViewHolder(
    private val binding: ItemIngredientsBinding,
    private val isClickable: Boolean = true,
    private val onItemClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: IngredientsModel) {
        binding.ingredientsModel = item
        binding.tvIngredients.isSelected = item.isSelected
        binding.invalidateAll()
        if (isClickable) {
            itemView.setOnClickListener {
                onItemClick(item.id)
            }
        }
    }
}