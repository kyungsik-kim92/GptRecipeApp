package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemShoppingListBinding
import com.example.presentation.model.ShoppingItemModel

class ShoppingListAdapter(
    private val onItemChecked: (ShoppingItemModel, Boolean) -> Unit
) : ListAdapter<ShoppingItemModel, ShoppingListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ItemShoppingListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShoppingListViewHolder(binding, onItemChecked)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ShoppingItemModel>() {
            override fun areItemsTheSame(
                oldItem: ShoppingItemModel,
                newItem: ShoppingItemModel
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ShoppingItemModel,
                newItem: ShoppingItemModel
            ): Boolean = oldItem == newItem
        }
    }
}

class ShoppingListViewHolder(
    private val binding: ItemShoppingListBinding,
    private val onItemChecked: (ShoppingItemModel, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ShoppingItemModel) {
        binding.apply {
            shoppingItem = item
            cbItem.setOnCheckedChangeListener(null)
            cbItem.isChecked = item.isChecked
            cbItem.setOnCheckedChangeListener { _, isChecked ->
                onItemChecked(item, isChecked)
            }
        }
    }
}