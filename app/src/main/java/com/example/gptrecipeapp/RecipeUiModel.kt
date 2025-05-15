package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel

data class RecipeUiModel(
    var id: Long,
    var searchKeyword: String,
    var ingredientsList: ArrayList<IngredientsModel>,
    var recipeList: ArrayList<RecipeModel>,
)