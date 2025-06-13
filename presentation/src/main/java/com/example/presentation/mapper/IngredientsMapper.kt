package com.example.presentation.mapper

import com.example.domain.model.Ingredient
import com.example.presentation.model.IngredientsModel

fun Ingredient.toPresentation() = IngredientsModel(
    id = this.id,
    ingredients = this.ingredients,
    isSelected = this.isSelected
)

fun IngredientsModel.toDomain() = Ingredient(
    id = this.id,
    isSelected = this.isSelected,
    ingredients = this.ingredients
)