package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ShoppingItemEntity
import com.example.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC")
    fun getAllShoppingLists(): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    suspend fun getShoppingListById(id: Long): ShoppingListEntity?

    @Query("SELECT * FROM shopping_lists WHERE recipeName = :recipeName")
    suspend fun getShoppingListByRecipe(recipeName: String): ShoppingListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(shoppingList: ShoppingListEntity): Long

    @Query("DELETE FROM shopping_lists WHERE id = :id")
    suspend fun deleteShoppingList(id: Long)

    @Query("DELETE FROM shopping_lists")
    suspend fun deleteAllShoppingLists()
}

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items WHERE shoppingListId = :listId ORDER BY category, name")
    fun getItemsByListId(listId: Long): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items")
    fun getAllItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingItemEntity>)

    @Query("UPDATE shopping_items SET isChecked = :isChecked WHERE id = :itemId")
    suspend fun updateItemChecked(itemId: Long, isChecked: Boolean)

    @Query("DELETE FROM shopping_items WHERE id = :itemId")
    suspend fun deleteItem(itemId: Long)

    @Query("DELETE FROM shopping_items WHERE shoppingListId = :listId")
    suspend fun deleteItemsByListId(listId: Long)

    @Query("DELETE FROM shopping_items")
    suspend fun deleteAllItems()

    @Query("DELETE FROM shopping_items WHERE isChecked = 1")
    suspend fun deleteCheckedItems()
}