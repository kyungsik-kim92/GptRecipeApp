package com.example.presentation.model

sealed class ShoppingListItem {
    data class Header(
        val category: String,
        val itemCount: Int,
        val checkedCount: Int,
        val isExpanded: Boolean = true
    ) : ShoppingListItem()

    data class Item(
        val shoppingItem: ShoppingItemModel
    ) : ShoppingListItem()
}