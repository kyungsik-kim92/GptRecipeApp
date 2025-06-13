package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SearchUiModel(
    var searchKeyword: String,
    var isFetched: Boolean,
    var isLoading: Boolean,
    var ingredientsList: ArrayList<IngredientsModel>,
) : Parcelable
