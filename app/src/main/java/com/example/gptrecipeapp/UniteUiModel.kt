package com.example.gptrecipeapp

import android.os.Parcelable
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class UniteUiModel(
    var isFetched: Boolean = false,
    var isLoading: Boolean = false,
    var searchKeyword: String = "",
    var searchKeywordList: ArrayList<String> = ArrayList(),
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
) : Parcelable
