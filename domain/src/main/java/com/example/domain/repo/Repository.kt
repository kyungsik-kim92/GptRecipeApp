package com.example.domain.repo

import com.example.domain.model.LocalRecipe
import com.example.domain.model.RecipeResponse
import com.example.domain.model.SearchHistory
import com.example.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun generateRecipe(prompt: String): RecipeResponse

    suspend fun isFavoriteByName(recipeName: String): Boolean

    suspend fun getAll(): List<LocalRecipe>

    suspend fun insertRecipe(recipe: LocalRecipe): Long

    suspend fun deleteRecipeByName(recipeName: String)

    suspend fun findRecipe(id: Long): LocalRecipe?

    suspend fun findRecipeByName(recipeName: String): LocalRecipe?

    fun getAllFavoritesFlow(): Flow<List<LocalRecipe>>

    fun getAllShoppingItems(): Flow<List<ShoppingItem>>

    suspend fun insertShoppingItems(items: List<ShoppingItem>)

    suspend fun updateShoppingItemChecked(itemId: Long, isChecked: Boolean)

    suspend fun deleteAllShoppingItems()

    suspend fun deleteCheckedShoppingItems()

    suspend fun insertShoppingItem(item: ShoppingItem)

    suspend fun deleteShoppingItem(itemId: Long)

    suspend fun hasShoppingItemsByRecipeName(recipeName: String): Boolean

    suspend fun insertSearchHistory(keyword: String)

    fun getRecentSearches(): Flow<List<SearchHistory>>

    suspend fun deleteSearchHistory(keyword: String)

    suspend fun deleteAllSearchHistory()
}