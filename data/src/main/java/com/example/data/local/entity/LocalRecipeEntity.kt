package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class LocalRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var searchKeyword: String = "",
    var ingredientsList: List<IngredientsEntity> = emptyList(),
    var recipeList: List<RecipeEntity> = emptyList(),
    var wellbeingRecipeList: List<WellbeingRecipeEntity> = emptyList(),
)