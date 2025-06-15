package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecIngredientsUiModel(
    var isLoading: Boolean,
    var isFetched: Boolean,
    var searchKeywordList: List<String>,
    var ingredientsList: List<IngredientsModel>,
) : Parcelable