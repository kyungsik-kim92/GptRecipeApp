package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FavoriteModel(
    val id: Long = 0,
    val searchKeyword: String = "",
    val ingredientsList: List<IngredientsModel> = emptyList(),
    val recipeList: List<RecipeModel> = emptyList(),
    val wellbeingRecipeList: List<WellbeingRecipeModel> = emptyList()
) : Parcelable