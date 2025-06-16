package com.example.presentation.model

data class FavoriteUiState(
    val favoriteList: List<FavoriteModel> = emptyList(),
    val selectedIngredients: Set<String> = emptySet(),
    val selectedRecipes: Set<String> = emptySet(),
    val selectedWellbeingRecipes: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)