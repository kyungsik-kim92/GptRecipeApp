package com.example.gptrecipeapp

import android.os.Parcelable
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class FavoriteModel(
    var id: Long = 0,
    var searchKeyword: String = "",
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
    var wellbeingRecipeList: ArrayList<WellbeingRecipeModel>
) : Parcelable
