package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gptrecipeapp.databinding.ItemIngredientsBinding
import com.example.gptrecipeapp.model.IngredientsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class IngredientsAdapter(
    private val isClickable: Boolean = true
) : ListAdapter<IngredientsModel, IngredientsViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding =
            ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsViewHolder(binding, isClickable)
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
    private val isClickable: Boolean = true
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: IngredientsModel) {
        binding.ingredientsModel = item
        CoroutineScope(Dispatchers.Main).launch {
            item.isSelected.collect { isSelected ->
                binding.tvIngredients.isSelected = isSelected
                binding.invalidateAll()
            }
        }
        if (isClickable){
            itemView.setOnClickListener {
                item.setIsSelected(!item.isSelected.value)
            }
        }
    }
}