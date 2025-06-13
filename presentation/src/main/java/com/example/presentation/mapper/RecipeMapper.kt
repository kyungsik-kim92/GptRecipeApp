package com.example.presentation.mapper

import com.example.data.local.entity.RecipeEntity
import com.example.domain.model.Recipe
import com.example.presentation.model.RecipeModel

fun Recipe.toPresentation() = RecipeModel(
    initialIsSelected = this.isSelected,
    recipe = this.recipe
)

fun RecipeModel.toDomain() = Recipe(
    isSelected = this.isSelected.value,
    recipe = this.recipe
)