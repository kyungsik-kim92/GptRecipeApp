package com.example.gptrecipeapp.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RecipeModel(
    val recipe: String,
    private val initialIsSelected: Boolean = false
) {
    private val _isSelected = MutableStateFlow(initialIsSelected)
    val isSelected: StateFlow<Boolean> = _isSelected.asStateFlow()

    fun setIsSelected(selected: Boolean) {
        _isSelected.value = selected
    }
}