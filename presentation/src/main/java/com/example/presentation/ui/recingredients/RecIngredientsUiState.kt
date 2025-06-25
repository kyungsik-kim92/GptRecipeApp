package com.example.presentation.ui.recingredients

import android.os.Parcelable
import com.example.presentation.model.IngredientsModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecIngredientsUiState(
    var isLoading: Boolean,
    var isFetched: Boolean,
    var searchKeywordList: List<String>,
    var ingredientsList: List<IngredientsModel>,
) : Parcelable