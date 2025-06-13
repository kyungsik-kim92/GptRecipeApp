package com.example.domain.model

data class LocalRecipe(
    val id: Long = 0,
    val searchKeyword: String = "",
    val ingredientsList: List<Ingredient> = emptyList(),
    val recipeList: List<Recipe> = emptyList(),
    val wellbeingRecipeList: List<WellbeingRecipe> = emptyList()
)