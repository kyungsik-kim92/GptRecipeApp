package com.example.data.local.entity

import androidx.room.Entity
import com.example.gptrecipeapp.model.RecipeModel


@Entity
data class RecipeEntity(
    var isSelected: Boolean,
    val recipe: String
)

fun RecipeEntity.toModel() = RecipeModel(
    initialIsSelected = this.isSelected,
    recipe = this.recipe
)
