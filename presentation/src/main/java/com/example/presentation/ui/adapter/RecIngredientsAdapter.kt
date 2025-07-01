package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemRecIngredientsBinding
import com.example.presentation.model.IngredientsModel

class RecIngredientsAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<IngredientsModel, RecIngredientsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecIngredientsViewHolder {
        val binding =
            ItemRecIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecIngredientsViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RecIngredientsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<IngredientsModel>() {
            override fun areItemsTheSame(
                oldItem: IngredientsModel,
                newItem: IngredientsModel
            ) = oldItem.id == newItem.id


            override fun areContentsTheSame(
                oldItem: IngredientsModel,
                newItem: IngredientsModel
            ) = oldItem == newItem

        }
    }
}


class RecIngredientsViewHolder(
    private val binding: ItemRecIngredientsBinding,
    private val onItemClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: IngredientsModel) {
        binding.ingredientsModel = item
        binding.tvIngredients.isSelected = item.isSelected
        binding.invalidateAll()
        itemView.setOnClickListener {
            onItemClick(item.id)
        }
    }
}