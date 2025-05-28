package com.example.gptrecipeapp.room.entity

import androidx.room.Entity


@Entity
data class RecipeEntity(
    var isSelected: Boolean,
    val recipe: String
)
