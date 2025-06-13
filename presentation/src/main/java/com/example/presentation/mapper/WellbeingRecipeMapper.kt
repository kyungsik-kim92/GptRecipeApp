package com.example.presentation.mapper

import com.example.data.local.entity.WellbeingRecipeEntity
import com.example.presentation.model.WellbeingRecipeModel

fun WellbeingRecipeModel.toEntity() = WellbeingRecipeEntity(
    wellbeingRecipe = this.wellbeingRecipe,
    isSelected = this.isSelected
)

//fun WellbeingRecipeEntity.toPresentation() = WellbeingRecipeModel(
//    isSelected = this.isSelected,
//    wellbeingRecipe = this.wellbeingRecipe
//)
//
