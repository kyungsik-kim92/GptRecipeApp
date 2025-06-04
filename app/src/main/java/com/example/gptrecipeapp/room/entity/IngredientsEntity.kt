package com.example.gptrecipeapp.room.entity

import androidx.room.Entity
import com.example.gptrecipeapp.model.IngredientsModel

@Entity
data class IngredientsEntity(
    var isSelected: Boolean,
    val ingredients: String
)

fun IngredientsEntity.toModel() = IngredientsModel(
    ingredients = this.ingredients,
    initialIsSelected = this.isSelected
)