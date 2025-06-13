package com.example.data.mapper

import com.example.data.local.entity.LocalRecipeEntity
import com.example.domain.model.LocalRecipe

fun LocalRecipeEntity.toDomain() = LocalRecipe(
    id = this.id,
    searchKeyword = this.searchKeyword,
    ingredientsList = this.ingredientsList.map { it.toDomain() },
    recipeList = this.recipeList.map { it.toDomain() },
    wellbeingRecipeList = this.wellbeingRecipeList.map { it.toDomain() }
)

fun LocalRecipe.toEntity() = LocalRecipeEntity(
    id = this.id,
    searchKeyword = this.searchKeyword,
    ingredientsList = ArrayList(this.ingredientsList.map { it.toEntity() }),
    recipeList = ArrayList(this.recipeList.map { it.toEntity() }),
    wellbeingRecipeList = ArrayList(this.wellbeingRecipeList.map { it.toEntity() })
)