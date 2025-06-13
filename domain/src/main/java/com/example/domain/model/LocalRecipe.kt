package com.example.domain.model

data class LocalRecipe(
    val id: Long = 0,
    val searchKeyword: String = "",
    val ingredientsList: ArrayList<Ingredient> = ArrayList(),
    val recipeList: ArrayList<Recipe> = ArrayList(),
    val wellbeingRecipeList: ArrayList<WellbeingRecipe> = ArrayList()
)