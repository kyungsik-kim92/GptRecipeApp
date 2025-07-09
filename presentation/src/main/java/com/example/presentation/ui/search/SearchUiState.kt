package com.example.presentation.ui.search

import android.os.Parcelable
import com.example.presentation.model.IngredientsModel
import kotlinx.parcelize.Parcelize


@Parcelize
sealed class SearchUiState : Parcelable {
    abstract val searchKeyword: String

    @Parcelize
    data class Loading(
        override val searchKeyword: String
    ) : SearchUiState()

    @Parcelize
    data class Success(
        override val searchKeyword: String,
        val ingredientsList: List<IngredientsModel>
    ) : SearchUiState()

    @Parcelize
    data class Error(
        override val searchKeyword: String,
        val message: String
    ) : SearchUiState()
}
sealed class SearchUiEvent {
    data class ShowSuccess(val message: String) : SearchUiEvent()
    data class ShowError(val message: String) : SearchUiEvent()
    data class RouteToIngredients(val searchUiState: SearchUiState.Success) : SearchUiEvent()
}
