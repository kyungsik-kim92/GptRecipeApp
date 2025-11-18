package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemShoppingCategoryHeaderBinding
import com.example.presentation.databinding.ItemShoppingListBinding
import com.example.presentation.model.ShoppingItemModel
import com.example.presentation.model.ShoppingListItem

class GroupedShoppingListAdapter(
    private val onItemChecked: (ShoppingItemModel, Boolean) -> Unit,
    private val onHeaderClicked: (String) -> Unit
) : ListAdapter<ShoppingListItem, RecyclerView.ViewHolder>(ShoppingListDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ShoppingListItem.Header -> VIEW_TYPE_HEADER
            is ShoppingListItem.Item -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemShoppingCategoryHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding, onHeaderClicked)
            }
            else -> {
                val binding = ItemShoppingListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GroupedShoppingViewHolder(binding, onItemChecked)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ShoppingListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ShoppingListItem.Item -> (holder as GroupedShoppingViewHolder).bind(item.shoppingItem)
        }
    }

    class HeaderViewHolder(
        private val binding: ItemShoppingCategoryHeaderBinding,
        private val onHeaderClicked: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: ShoppingListItem.Header) {
            binding.header = header
            binding.root.setOnClickListener {
                onHeaderClicked(header.category)
            }
            binding.executePendingBindings()
        }
    }
    class ShoppingListDiffCallback : DiffUtil.ItemCallback<ShoppingListItem>() {
        override fun areItemsTheSame(
            oldItem: ShoppingListItem,
            newItem: ShoppingListItem
        ): Boolean {
            return when {
                oldItem is ShoppingListItem.Header && newItem is ShoppingListItem.Header ->
                    oldItem.category == newItem.category

                oldItem is ShoppingListItem.Item && newItem is ShoppingListItem.Item ->
                    oldItem.shoppingItem.id == newItem.shoppingItem.id

                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: ShoppingListItem,
            newItem: ShoppingListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    class GroupedShoppingViewHolder(
        private val binding: ItemShoppingListBinding,
        private val onItemChecked: (ShoppingItemModel, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingItemModel) {
            binding.shoppingItem = item
            binding.cbItem.setOnCheckedChangeListener(null)
            binding.cbItem.isChecked = item.isChecked
            binding.cbItem.setOnCheckedChangeListener { _, isChecked ->
                onItemChecked(item, isChecked)
            }
            binding.executePendingBindings()
        }
    }
}

