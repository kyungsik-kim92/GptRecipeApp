package com.example.gptrecipeapp.room.entity

import androidx.room.Entity


@Entity
data class WellbeingRecipeEntity(
    var isSelected: Boolean,
    val wellBeingRecipe: String
)
