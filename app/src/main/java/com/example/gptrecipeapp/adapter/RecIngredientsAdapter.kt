package com.example.gptrecipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gptrecipeapp.databinding.ItemRecIngredientsBinding
import com.example.gptrecipeapp.model.IngredientsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecIngredientsAdapter : ListAdapter<IngredientsModel, IngredientsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding =
            ItemRecIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<IngredientsModel>() {
            override fun areItemsTheSame(
                oldItem: IngredientsModel,
                newItem: IngredientsModel
            ) =
                oldItem == newItem


            override fun areContentsTheSame(
                oldItem: IngredientsModel,
                newItem: IngredientsModel
            ) = oldItem == newItem

        }
    }
}


class IngredientsViewHolder(
    private val binding: ItemRecIngredientsBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: IngredientsModel) {
        binding.ingredientsModel = item
        CoroutineScope(Dispatchers.Main).launch {
            item.isSelected.collect { isSelected ->
                binding.tvIngredients.isSelected = isSelected
                binding.invalidateAll()
            }
        }
        itemView.setOnClickListener {
            item.setIsSelected(!item.isSelected.value)
        }
    }
}