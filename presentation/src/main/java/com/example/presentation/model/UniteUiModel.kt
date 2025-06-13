package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UniteUiModel(
    var isFetched: Boolean = false,
    var isLoading: Boolean = false,
    var searchKeyword: String = "",
    var searchKeywordList: ArrayList<String> = ArrayList(),
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
    var wellbeingRecipeList: ArrayList<WellbeingRecipeModel>
) : Parcelable
