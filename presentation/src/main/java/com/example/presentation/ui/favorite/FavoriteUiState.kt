package com.example.presentation.ui.favorite

import com.example.presentation.model.FavoriteModel

data class FavoriteUiState(
    val favoriteList: List<FavoriteModel> = emptyList(),
    val selectedIngredients: Set<String> = emptySet(),
    val selectedRecipes: Set<String> = emptySet(),
    val selectedWellbeingRecipes: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)