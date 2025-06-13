package com.example.presentation.mapper

import com.example.data.local.entity.RecipeEntity
import com.example.presentation.model.RecipeModel

fun RecipeModel.toEntity() = RecipeEntity(
    recipe = this.recipe,
    isSelected = this.isSelected.value
)

fun RecipeEntity.toModel() = RecipeModel(
    initialIsSelected = this.isSelected,
    recipe = this.recipe
)
