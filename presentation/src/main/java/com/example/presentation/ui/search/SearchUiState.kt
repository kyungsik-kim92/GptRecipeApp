package com.example.presentation.ui.search

import android.os.Parcelable
import com.example.presentation.model.IngredientsModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class SearchUiState(
    var searchKeyword: String,
    var isFetched: Boolean,
    var isLoading: Boolean,
    var ingredientsList: List<IngredientsModel>,
) : Parcelable
