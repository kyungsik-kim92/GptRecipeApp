package com.example.presentation.ui.recipe

import android.os.Parcelable
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.WellbeingRecipeModel
import kotlinx.parcelize.Parcelize


@Parcelize
sealed class RecipeUiState : Parcelable {
    abstract val id: Long
    abstract val searchKeyword: String
    abstract val ingredientsList: List<IngredientsModel>
    abstract val recipeList: List<RecipeModel>
    abstract val isSubscribe: Boolean
    abstract val wellbeingRecipeModel: List<WellbeingRecipeModel>

    @Parcelize
    data class Idle(
        override val id: Long = 0L,
        override val searchKeyword: String = "",
        override val ingredientsList: List<IngredientsModel> = emptyList(),
        override val recipeList: List<RecipeModel> = emptyList(),
        override val isSubscribe: Boolean = false,
        override val wellbeingRecipeModel: List<WellbeingRecipeModel> = emptyList()
    ) : RecipeUiState()

    @Parcelize
    data class Loading(
        override val id: Long,
        override val searchKeyword: String,
        override val ingredientsList: List<IngredientsModel>,
        override val recipeList: List<RecipeModel>,
        override val isSubscribe: Boolean,
        override val wellbeingRecipeModel: List<WellbeingRecipeModel>
    ) : RecipeUiState()

    @Parcelize
    data class Success(
        override val id: Long,
        override val searchKeyword: String,
        override val ingredientsList: List<IngredientsModel>,
        override val recipeList: List<RecipeModel>,
        override val isSubscribe: Boolean,
        override val wellbeingRecipeModel: List<WellbeingRecipeModel>
    ) : RecipeUiState()

    @Parcelize
    data class Error(
        override val id: Long,
        override val searchKeyword: String,
        override val ingredientsList: List<IngredientsModel>,
        override val recipeList: List<RecipeModel>,
        override val isSubscribe: Boolean,
        override val wellbeingRecipeModel: List<WellbeingRecipeModel>,
        val message: String
    ) : RecipeUiState()
}
sealed class RecipeUiEvent {
    data class ShowSuccess(val message: String) : RecipeUiEvent()
    data class ShowError(val message: String) : RecipeUiEvent()
    data class RouteToWellbeing(val recipeUiState: RecipeUiState) : RecipeUiEvent()
}