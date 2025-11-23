package com.example.data.database.source.impl

import com.example.data.database.dao.RecipeDao
import com.example.data.database.dao.SearchHistoryDao
import com.example.data.database.dao.ShoppingListDao
import com.example.data.database.network.ApiService
import com.example.data.database.source.DataSource
import com.example.data.local.entity.LocalRecipeEntity
import com.example.data.local.entity.SearchHistoryEntity
import com.example.data.local.entity.ShoppingItemEntity
import com.example.data.remote.dto.GPT
import com.example.data.remote.dto.GptRequestParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val recipeDao: RecipeDao,
    private val shoppingListDao: ShoppingListDao,
    private val searchHistoryDao: SearchHistoryDao
) : DataSource {
    override suspend fun getGptResponse(body: GptRequestParam): GPT {
        return apiService.getGptResponse(body)
    }

    override suspend fun getAll(): List<LocalRecipeEntity> {
        return recipeDao.getAll()
    }

    override suspend fun insertRecipe(recipe: LocalRecipeEntity): Long {
        return recipeDao.insertRecipe(recipe)
    }

    override suspend fun deleteRecipeByName(recipeName: String) {
        return recipeDao.deleteRecipeByName(recipeName)
    }

    override suspend fun findRecipeByName(recipeName: String): LocalRecipeEntity? {
        return recipeDao.findRecipeByName(recipeName)
    }

    override suspend fun isFavoriteByName(recipeName: String): Boolean {
        return recipeDao.findRecipeByName(recipeName) != null
    }

    override suspend fun findRecipe(id: Long): LocalRecipeEntity? {
        return recipeDao.findRecipe(id)
    }

    override fun getAllFavoritesFlow(): Flow<List<LocalRecipeEntity>> {
        return recipeDao.getAllFavoritesFlow()
    }

    override fun getAllShoppingItems(): Flow<List<ShoppingItemEntity>> {
        return shoppingListDao.getAllItems()
    }

    override suspend fun insertShoppingItems(items: List<ShoppingItemEntity>) {
        shoppingListDao.insertItems(items)
    }

    override suspend fun updateShoppingItemChecked(itemId: Long, isChecked: Boolean) {
        shoppingListDao.updateItemChecked(itemId, isChecked)
    }

    override suspend fun deleteAllShoppingItems() {
        shoppingListDao.deleteAllItems()
    }

    override suspend fun deleteCheckedShoppingItems() {
        shoppingListDao.deleteCheckedItems()
    }

    override suspend fun insertShoppingItem(item: ShoppingItemEntity) {
        shoppingListDao.insertItem(item)
    }

    override suspend fun deleteShoppingItem(itemId: Long) {
        shoppingListDao.deleteItem(itemId)
    }

    override suspend fun hasShoppingItemsByRecipeName(recipeName: String): Boolean {
        return shoppingListDao.hasItemsByRecipeName(recipeName)
    }

    override suspend fun insertSearchHistory(keyword: String) {
        val entity = SearchHistoryEntity(
            keyword = keyword,
            searchedAt = System.currentTimeMillis()
        )
        searchHistoryDao.insertSearch(entity)
    }

    override fun getRecentSearches(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches()
    }

    override suspend fun deleteSearchHistory(keyword: String) {
        searchHistoryDao.deleteSearch(keyword)
    }

    override suspend fun deleteAllSearchHistory() {
        searchHistoryDao.deleteAllSearches()
    }
}