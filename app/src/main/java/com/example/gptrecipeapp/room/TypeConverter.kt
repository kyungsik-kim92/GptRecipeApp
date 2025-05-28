package com.example.gptrecipeapp.room

import androidx.room.TypeConverter
import com.example.gptrecipeapp.room.entity.IngredientsEntity
import com.example.gptrecipeapp.room.entity.RecipeEntity
import com.example.gptrecipeapp.room.entity.WellbeingRecipeEntity
import com.google.gson.Gson

class CustomTypeConverters {

    @TypeConverter
    fun ingredientsListToJson(value: ArrayList<IngredientsEntity>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToIngredientsList(value: String): ArrayList<IngredientsEntity>? {
        return Gson().fromJson(value, Array<IngredientsEntity>::class.java)?.toList()
            ?.let { ArrayList(it) }
    }

    @TypeConverter
    fun recipeListToJson(value: ArrayList<RecipeEntity>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToRecipeList(value: String): ArrayList<RecipeEntity>? {
        return Gson().fromJson(value, Array<RecipeEntity>::class.java)?.toList()
            ?.let { ArrayList(it) }
    }

    @TypeConverter
    fun wellBeingRecipeListToJson(value: ArrayList<WellbeingRecipeEntity>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToWellBeingRecipeList(value: String): ArrayList<WellbeingRecipeEntity>? {
        return Gson().fromJson(value, Array<WellbeingRecipeEntity>::class.java)?.toList()
            ?.let { ArrayList(it) }
    }
}