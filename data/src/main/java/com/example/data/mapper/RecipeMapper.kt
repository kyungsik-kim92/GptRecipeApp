package com.example.data.mapper

import com.example.data.local.entity.RecipeEntity
import com.example.domain.model.Recipe

fun RecipeEntity.toDomain() = Recipe(
    isSelected = this.isSelected,
    recipe = this.recipe
)

fun Recipe.toEntity() = RecipeEntity(
    isSelected = this.isSelected,
    recipe = this.recipe
)