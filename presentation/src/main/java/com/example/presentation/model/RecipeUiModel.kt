package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecipeUiModel(
    var id: Long,
    var searchKeyword: String,
    var ingredientsList: List<IngredientsModel>,
    var recipeList: List<RecipeModel>,
    var isLoading: Boolean,
    var isSubscribe: Boolean,
    var wellbeingRecipeModel: List<WellbeingRecipeModel>
) : Parcelable