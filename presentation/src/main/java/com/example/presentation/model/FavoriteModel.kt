package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FavoriteModel(
    var id: Long = 0,
    var searchKeyword: String = "",
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
    var wellbeingRecipeList: ArrayList<WellbeingRecipeModel>
) : Parcelable
