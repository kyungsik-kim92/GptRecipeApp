package com.example.gptrecipeapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gptrecipeapp.FavoriteModel


@Entity
data class LocalRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var searchKeyword: String = "",
    var ingredientsList: ArrayList<IngredientsEntity> = ArrayList(),
    var recipeList: ArrayList<RecipeEntity> = ArrayList(),
    var wellbeingRecipeList: ArrayList<WellbeingRecipeEntity> = ArrayList(),
)

fun LocalRecipeEntity.toFavoriteModel() = FavoriteModel(
    id = this.id,
    searchKeyword = this.searchKeyword,
)