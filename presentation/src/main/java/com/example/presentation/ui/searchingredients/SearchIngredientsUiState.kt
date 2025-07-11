package com.example.presentation.ui.searchingredients

import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.model.WellbeingRecipeModel

sealed class SearchIngredientsUiState {
    abstract val searchKeyword: String
    abstract val ingredientsList: List<IngredientsModel>

    data class Idle(
        override val searchKeyword: String = "",
        override val ingredientsList: List<IngredientsModel> = emptyList()
    ) : SearchIngredientsUiState()

    data class Loading(
        override val searchKeyword: String,
        override val ingredientsList: List<IngredientsModel>
    ) : SearchIngredientsUiState()

    data class Success(
        override val searchKeyword: String,
        override val ingredientsList: List<IngredientsModel>,
        val recipeList: List<RecipeModel>,
        val wellbeingRecipeList: List<WellbeingRecipeModel>
    ) : SearchIngredientsUiState()

    data class Error(
        override val searchKeyword: String,
        override val ingredientsList: List<IngredientsModel>,
        val message: String
    ) : SearchIngredientsUiState()
}

sealed class SearchIngredientsUiEvent {
    data class ShowSuccess(val message: String) : SearchIngredientsUiEvent()
    data class ShowError(val message: String) : SearchIngredientsUiEvent()
    data class RouteToRecipe(val uniteUiState: UniteUiState) : SearchIngredientsUiEvent()
}