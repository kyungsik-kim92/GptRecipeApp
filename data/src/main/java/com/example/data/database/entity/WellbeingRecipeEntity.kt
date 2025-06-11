package com.example.data.database.entity

import androidx.room.Entity
import com.example.gptrecipeapp.WellbeingRecipeModel


@Entity
data class WellbeingRecipeEntity(
    var isSelected: Boolean,
    val wellbeingRecipe: String
)

fun WellbeingRecipeEntity.toModel() = WellbeingRecipeModel(
    initialIsSelected = this.isSelected,
    wellbeingRecipe = this.wellbeingRecipe
)