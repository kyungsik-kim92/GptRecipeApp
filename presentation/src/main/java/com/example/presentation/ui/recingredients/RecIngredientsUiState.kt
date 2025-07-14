package com.example.presentation.ui.recingredients

import android.os.Parcelable
import com.example.presentation.model.IngredientsModel
import kotlinx.parcelize.Parcelize


@Parcelize
sealed class RecIngredientsUiState : Parcelable {
    abstract val searchKeywordList: List<String>
    abstract val ingredientsList: List<IngredientsModel>

    @Parcelize
    data class Idle(
        override val searchKeywordList: List<String> = emptyList(),
        override val ingredientsList: List<IngredientsModel> = emptyList()
    ) : RecIngredientsUiState()

    @Parcelize
    data class Loading(
        override val searchKeywordList: List<String>,
        override val ingredientsList: List<IngredientsModel>
    ) : RecIngredientsUiState()

    @Parcelize
    data class Success(
        override val searchKeywordList: List<String>,
        override val ingredientsList: List<IngredientsModel>
    ) : RecIngredientsUiState()

    @Parcelize
    data class Error(
        override val searchKeywordList: List<String>,
        override val ingredientsList: List<IngredientsModel>,
        val message: String
    ) : RecIngredientsUiState()
}

@Parcelize
sealed class RecIngredientsUiEvent : Parcelable {
    @Parcelize
    data class ShowSuccess(val message: String) : RecIngredientsUiEvent()

    @Parcelize
    data class ShowError(val message: String) : RecIngredientsUiEvent()

    @Parcelize
    data class RouteToRecRecipe(val recIngredientsUiState: RecIngredientsUiState.Success) : RecIngredientsUiEvent()
}