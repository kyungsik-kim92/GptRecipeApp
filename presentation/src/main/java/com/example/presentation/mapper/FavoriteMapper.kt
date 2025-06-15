package com.example.presentation.mapper

import com.example.domain.model.LocalRecipe
import com.example.presentation.model.FavoriteModel

fun LocalRecipe.toFavoriteModel() = FavoriteModel(
    id = this.id,
    searchKeyword = this.searchKeyword,
    ingredientsList = this.ingredientsList.map { it.toPresentation() },
    recipeList = this.recipeList.map { it.toPresentation() },
    wellbeingRecipeList = this.wellbeingRecipeList.map { it.toPresentation() }
)

fun FavoriteModel.toDomain() = LocalRecipe(
    id = this.id,
    searchKeyword = this.searchKeyword,
    ingredientsList = this.ingredientsList.map { it.toDomain() },
    recipeList = this.recipeList.map { it.toDomain() },
    wellbeingRecipeList = this.wellbeingRecipeList.map { it.toDomain() }
)