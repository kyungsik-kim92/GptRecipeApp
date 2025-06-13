package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecipeUiModel(
    var id: Long,
    var searchKeyword: String,
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
    var isLoading: Boolean,
    var isSubscribe: Boolean,
    var wellbeingRecipeModel: ArrayList<WellbeingRecipeModel>
) : Parcelable