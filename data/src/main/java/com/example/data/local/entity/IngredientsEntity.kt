package com.example.data.local.entity

import androidx.room.Entity

@Entity
data class IngredientsEntity(
    var isSelected: Boolean,
    val ingredients: String
)