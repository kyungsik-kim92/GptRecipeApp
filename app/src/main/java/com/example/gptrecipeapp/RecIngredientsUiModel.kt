package com.example.gptrecipeapp

import android.os.Parcelable
import com.example.gptrecipeapp.model.IngredientsModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecIngredientsUiModel(
    var isLoading: Boolean,
    var isFetched: Boolean,
    var searchKeywordList: ArrayList<String>,
    var ingredientsList: ArrayList<IngredientsModel>,
):Parcelable