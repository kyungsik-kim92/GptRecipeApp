package com.example.presentation.mapper

import com.example.data.local.entity.IngredientsEntity
import com.example.presentation.model.IngredientsModel

fun IngredientsModel.toEntity() = IngredientsEntity(
    ingredients = this.ingredients,
    isSelected = this.isSelected.value
)

fun IngredientsEntity.toModel() = IngredientsModel(
    ingredients = this.ingredients,
    initialIsSelected = this.isSelected
)