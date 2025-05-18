package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel

data class RecRecipeUiModel(
    var isFetched: Boolean = false,
    var isLoading: Boolean = false,
    var searchKeyword: String = "",
    var searchKeywordList: ArrayList<String> = ArrayList(),
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
)
