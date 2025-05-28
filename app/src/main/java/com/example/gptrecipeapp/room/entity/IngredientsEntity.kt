package com.example.gptrecipeapp.room.entity

import androidx.room.Entity

@Entity
data class IngredientsEntity(
    var isSelected: Boolean,
    val ingredients: String
)