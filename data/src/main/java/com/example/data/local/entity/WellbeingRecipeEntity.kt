package com.example.data.local.entity

import androidx.room.Entity


@Entity
data class WellbeingRecipeEntity(
    var isSelected: Boolean,
    val wellbeingRecipe: String
)