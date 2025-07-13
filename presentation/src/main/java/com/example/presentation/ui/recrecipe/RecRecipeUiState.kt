package com.example.presentation.ui.recrecipe

import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.model.WellbeingRecipeModel

sealed class RecRecipeUiState {
    abstract val searchKeyword: String
    abstract val searchKeywordList: List<String>
    abstract val ingredientsList: List<IngredientsModel>

    data class Idle(
        override val searchKeyword: String = "",
        override val searchKeywordList: List<String> = emptyList(),
        override val ingredientsList: List<IngredientsModel> = emptyList()
    ) : RecRecipeUiState()

    data class Loading(
        override val searchKeyword: String,
        override val searchKeywordList: List<String>,
        override val ingredientsList: List<IngredientsModel>
    ) : RecRecipeUiState()

    data class Success(
        override val searchKeyword: String,
        override val searchKeywordList: List<String>,
        override val ingredientsList: List<IngredientsModel>,
        val recipeList: List<RecipeModel>,
        val wellbeingRecipeList: List<WellbeingRecipeModel>
    ) : RecRecipeUiState()

    data class Error(
        override val searchKeyword: String,
        override val searchKeywordList: List<String>,
        override val ingredientsList: List<IngredientsModel>,
        val message: String
    ) : RecRecipeUiState()
}

sealed class RecRecipeUiEvent {
    data class ShowSuccess(val message: String) : RecRecipeUiEvent()
    data class ShowError(val message: String) : RecRecipeUiEvent()
    data class RouteToRecipe(val uniteUiState: UniteUiState) : RecRecipeUiEvent()
}