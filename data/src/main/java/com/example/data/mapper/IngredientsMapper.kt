package com.example.data.mapper

import com.example.data.local.entity.IngredientsEntity
import com.example.domain.model.Ingredient

fun IngredientsEntity.toDomain() = Ingredient(
    id = this.ingredients,
    isSelected = this.isSelected,
    ingredients = this.ingredients
)

fun Ingredient.toEntity() = IngredientsEntity(
    isSelected = this.isSelected,
    ingredients = this.ingredients
)