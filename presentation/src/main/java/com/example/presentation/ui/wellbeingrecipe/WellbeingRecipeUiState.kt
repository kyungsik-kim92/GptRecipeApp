package com.example.presentation.ui.wellbeingrecipe

import com.example.presentation.model.WellbeingRecipeModel

sealed class WellbeingRecipeUiState {
    data object Loading : WellbeingRecipeUiState()

    data class Success(
        val wellBeingRecipeList: List<WellbeingRecipeModel>
    ) : WellbeingRecipeUiState()

    data class Error(val message: String) : WellbeingRecipeUiState()
}
