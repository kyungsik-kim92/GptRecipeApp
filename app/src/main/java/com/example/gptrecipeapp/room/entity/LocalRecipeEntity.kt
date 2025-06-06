package com.example.gptrecipeapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gptrecipeapp.FavoriteModel
import com.example.gptrecipeapp.WellbeingRecipeModel
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel


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
    ingredientsList = this.ingredientsList.map { it.toModel() } as ArrayList<IngredientsModel>,
    recipeList = this.recipeList.map { it.toModel() } as ArrayList<RecipeModel>,
    wellbeingRecipeList = this.wellbeingRecipeList.map { it.toModel() } as ArrayList<WellbeingRecipeModel>
)