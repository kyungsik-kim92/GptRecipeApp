package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ShoppingListDao {

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

    @Query("DELETE FROM shopping_items")
    suspend fun deleteAllItems()

    @Query("DELETE FROM shopping_items WHERE isChecked = 1")
    suspend fun deleteCheckedItems()

    @Query("SELECT EXISTS(SELECT 1 FROM shopping_items WHERE recipeName = :recipeName LIMIT 1)")
    suspend fun hasItemsByRecipeName(recipeName: String): Boolean
}