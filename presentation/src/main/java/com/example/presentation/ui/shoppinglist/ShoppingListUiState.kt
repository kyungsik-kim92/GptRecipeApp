package com.example.presentation.ui.shoppinglist

import com.example.presentation.model.ShoppingItemModel

sealed class ShoppingListUiState {
    data object Idle : ShoppingListUiState()

    data object Loading : ShoppingListUiState()

    data class Success(
        val items: List<ShoppingItemModel>,
        val totalCount: Int,
        val checkedCount: Int
    ) : ShoppingListUiState()

    data class Error(
        val message: String
    ) : ShoppingListUiState()
}