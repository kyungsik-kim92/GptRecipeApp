package com.example.presentation.mapper

import com.example.data.local.entity.WellbeingRecipeEntity
import com.example.domain.model.WellbeingRecipe
import com.example.presentation.model.WellbeingRecipeModel

fun WellbeingRecipe.toPresentation() = WellbeingRecipeModel(
    id = this.recipe,
    wellbeingRecipe = this.recipe,
    isSelected = this.isSelected
)

fun WellbeingRecipeModel.toDomain() = WellbeingRecipe(
    recipe = this.wellbeingRecipe,
    isSelected = this.isSelected
)