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
sealed class ShoppingListEvent {
    data class ShowSuccess(val message: String) : ShoppingListEvent()
    data class ShowError(val message: String) : ShoppingListEvent()
    object ShowDeleteAllConfirmation : ShoppingListEvent()
}
