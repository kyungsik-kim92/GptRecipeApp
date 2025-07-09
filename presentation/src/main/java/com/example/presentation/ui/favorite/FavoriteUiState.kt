package com.example.presentation.ui.favorite

import com.example.presentation.model.FavoriteModel

sealed class FavoriteUiState {
    data object Loading : FavoriteUiState()

    data class Success(
        val favoriteList: List<FavoriteModel>,
        val selectedIngredients: Set<String> = emptySet(),
        val selectedRecipes: Set<String> = emptySet(),
        val selectedWellbeingRecipes: Set<String> = emptySet()
    ) : FavoriteUiState()

    data class Error(val message: String) : FavoriteUiState()
}

sealed class FavoriteUiEvent {
    data class ShowSuccess(val message: String) : FavoriteUiEvent()
    data class ShowError(val message: String) : FavoriteUiEvent()
    data class NavigateToRecipe(val favoriteModel: FavoriteModel) : FavoriteUiEvent()
}