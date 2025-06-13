package com.example.presentation.mapper

import com.example.domain.model.Ingredient
import com.example.presentation.model.IngredientsModel

fun Ingredient.toPresentation() = IngredientsModel(
    ingredients = this.ingredients,
    initialIsSelected = this.isSelected
)

fun IngredientsModel.toDomain() = Ingredient(
    isSelected = this.isSelected.value,
    ingredients = this.ingredients
)