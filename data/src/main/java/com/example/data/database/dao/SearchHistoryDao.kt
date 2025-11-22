package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE keyword = :keyword")
    suspend fun deleteSearch(keyword: String)

    @Query("DELETE FROM search_history")
    suspend fun deleteAllSearches()

    @Query("SELECT EXISTS(SELECT 1 FROM search_history WHERE keyword = :keyword)")
    suspend fun isSearchExists(keyword: String): Boolean
}