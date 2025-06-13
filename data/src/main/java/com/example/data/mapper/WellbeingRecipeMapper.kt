package com.example.data.mapper

import com.example.data.local.entity.WellbeingRecipeEntity
import com.example.domain.model.WellbeingRecipe

fun WellbeingRecipeEntity.toDomain() = WellbeingRecipe(
    isSelected = this.isSelected,
    recipe = this.wellbeingRecipe
)

fun WellbeingRecipe.toEntity() = WellbeingRecipeEntity(
    isSelected = this.isSelected,
    wellbeingRecipe = this.recipe
)