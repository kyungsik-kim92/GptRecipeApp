package com.example.gptrecipeapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class LocalRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var searchKeyword: String = "",
    var ingredientsList: ArrayList<IngredientsEntity> = ArrayList(),
    var recipeList: ArrayList<RecipeEntity> = ArrayList(),
    var wellBeingRecipeList: ArrayList<WellBeingRecipeEntity> = ArrayList(),
)