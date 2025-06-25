package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UniteUiState(
    var isFetched: Boolean = false,
    var isLoading: Boolean = false,
    var searchKeyword: String = "",
    var searchKeywordList: List<String> = emptyList(),
    var ingredientsList: List<IngredientsModel>,
    var recipeList: List<RecipeModel>,
    var wellbeingRecipeList: List<WellbeingRecipeModel>
) : Parcelable
