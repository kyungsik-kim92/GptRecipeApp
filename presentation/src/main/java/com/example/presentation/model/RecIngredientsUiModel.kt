package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecIngredientsUiModel(
    var isLoading: Boolean,
    var isFetched: Boolean,
    var searchKeywordList: ArrayList<String>,
    var ingredientsList: ArrayList<IngredientsModel>,
) : Parcelable