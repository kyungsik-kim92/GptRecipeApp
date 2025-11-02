package com.example.presentation.ui.shoppinglist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class ShoppingListViewModel(
) : ViewModel() {
    private val _uiState = MutableStateFlow<ShoppingListUiState>(ShoppingListUiState.Idle)
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()
}