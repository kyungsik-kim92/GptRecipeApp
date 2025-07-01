package com.example.presentation.ui.recipe

import android.os.Parcelable
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.WellbeingRecipeModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecipeUiState(
    var id: Long,
    var searchKeyword: String,
    var ingredientsList: List<IngredientsModel>,
    var recipeList: List<RecipeModel>,
    var isLoading: Boolean,
    var isSubscribe: Boolean,
    var wellbeingRecipeModel: List<WellbeingRecipeModel> = emptyList(),
) : Parcelable