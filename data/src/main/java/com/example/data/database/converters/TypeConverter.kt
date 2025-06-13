package com.example.data.database.converters

import androidx.room.TypeConverter
import com.example.data.local.entity.IngredientsEntity
import com.example.data.local.entity.RecipeEntity
import com.example.data.local.entity.WellbeingRecipeEntity
import com.google.gson.Gson

class CustomTypeConverters {

    @TypeConverter
    fun ingredientsListToJson(value: List<IngredientsEntity>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToIngredientsList(value: String): List<IngredientsEntity>? {
        return Gson().fromJson(value, Array<IngredientsEntity>::class.java)?.toList()
    }

    @TypeConverter
    fun recipeListToJson(value: List<RecipeEntity>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToRecipeList(value: String): List<RecipeEntity>? {
        return Gson().fromJson(value, Array<RecipeEntity>::class.java)?.toList()
    }

    @TypeConverter
    fun wellBeingRecipeListToJson(value: List<WellbeingRecipeEntity>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToWellBeingRecipeList(value: String): List<WellbeingRecipeEntity>? {
        return Gson().fromJson(value, Array<WellbeingRecipeEntity>::class.java)?.toList()
    }
}