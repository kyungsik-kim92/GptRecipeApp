package com.example.gptrecipeapp

import android.os.Parcelable
import com.example.gptrecipeapp.model.IngredientsModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class SearchUiModel(
    var searchKeyword: String,
    var isFetched: Boolean,
    var isLoading: Boolean,
    var ingredientsList: ArrayList<IngredientsModel>,
):Parcelable
