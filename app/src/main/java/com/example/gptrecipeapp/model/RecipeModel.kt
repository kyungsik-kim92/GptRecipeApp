package com.example.gptrecipeapp.model

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecipeModel(
    val recipe: String,
    private val initialIsSelected: Boolean = false
) : Parcelable {
    private val _isSelected = MutableStateFlow(initialIsSelected)
    val isSelected: StateFlow<Boolean> = _isSelected.asStateFlow()

    fun setIsSelected(selected: Boolean) {
        _isSelected.value = selected
    }
}