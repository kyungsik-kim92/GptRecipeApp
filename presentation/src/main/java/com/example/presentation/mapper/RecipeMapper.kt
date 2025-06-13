package com.example.presentation.mapper

import com.example.domain.model.Recipe
import com.example.presentation.model.RecipeModel

fun Recipe.toPresentation() = RecipeModel(
    id = this.id,
    isSelected = this.isSelected,
    recipe = this.recipe
)

fun RecipeModel.toDomain() = Recipe(
    id = this.id,
    isSelected = this.isSelected,
    recipe = this.recipe
)