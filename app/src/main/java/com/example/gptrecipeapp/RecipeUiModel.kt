package com.example.gptrecipeapp

import android.os.Parcelable
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecipeUiModel(
    var id: Long,
    var searchKeyword: String,
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
    var isLoading: Boolean,
    var wellbeingRecipeModel: ArrayList<WellbeingRecipeModel>
) : Parcelable